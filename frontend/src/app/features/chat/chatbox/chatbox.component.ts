import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  AfterViewChecked,
  ChangeDetectorRef,
  AfterViewInit,
  OnDestroy,
} from '@angular/core';
import {
  ChatService,
  ChatResponse,
  ChatSession,
  ChatMessage,
} from '../../../core/services/chat.service';
import { Title } from '@angular/platform-browser';
import { Subscription } from 'rxjs';

@Component({
  standalone: false,
  selector: 'app-chatbox',
  templateUrl: './chatbox.component.html',
  styleUrls: ['./chatbox.component.scss'],
})
export class ChatboxComponent
  implements OnInit, AfterViewChecked, AfterViewInit, OnDestroy
{
  sessions: ChatSession[] = [];
  activeSessionId: string | null = null;
  activeSessionMessages: ChatMessage[] = [];

  newMessage: string = '';
  isProcessing: boolean = false;
  isBlocked: boolean = false;
  private sub!: Subscription;

  @ViewChild('scrollMe') private myScrollContainer!: ElementRef;

  constructor(
    private chatService: ChatService,
    private titleService: Title,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.titleService.setTitle('DataPulse | Support Chat');

    // Command the service to grab data from
    this.chatService.loadInitialHistory();

    // RxJS Stream - UI auto updates instantly avoiding angular change detection hang
    this.sub = this.chatService.sessions$.subscribe((stream) => {
      this.sessions = stream;

      if (this.sessions.length > 0) {
        if (!this.activeSessionId) {
          this.activeSessionId = this.sessions[0].id;
        }
        this.updateActiveMessages();
      } else if (!this.isProcessing) {
        // Queue the brand new session start after zone is cleared
        setTimeout(() => this.startNewSession(), 0);
      }
      this.cdr.detectChanges();
    });
  }

  ngOnDestroy(): void {
    if (this.sub) this.sub.unsubscribe();
  }

  ngAfterViewInit() {
    setTimeout(() => this.cdr.detectChanges(), 50);
  }

  ngAfterViewChecked() {
    this.scrollToBottom();
  }

  startNewSession() {
    const newSession: ChatSession = {
      id: Date.now().toString(),
      title: 'New Chat',
      messages: [
        {
          content: 'Hello! How can I help you today?',
          role: 'ai',
        },
      ],
    };

    // Create new array to satisfy Angular immutability checks
    const updated = [newSession, ...this.sessions];
    this.activeSessionId = newSession.id;
    this.chatService.saveSessions(updated);
  }

  selectSession(id: string) {
    this.activeSessionId = id;
    this.updateActiveMessages();
  }

  deleteSession(event: Event, id: string) {
    event.stopPropagation();

    if (this.activeSessionId === id) {
      this.activeSessionId = null;
      this.activeSessionMessages = [];
    }
    this.chatService.deleteSession(id);
  }

  updateActiveMessages() {
    const session = this.sessions.find((s) => s.id === this.activeSessionId);
    this.activeSessionMessages = session ? session.messages : [];
  }

  scrollToBottom(): void {
    try {
      this.myScrollContainer.nativeElement.scrollTop =
        this.myScrollContainer.nativeElement.scrollHeight;
    } catch (err) {}
  }

  sendMessage() {
    if (!this.newMessage.trim() || this.isProcessing || !this.activeSessionId)
      return;

    const session = this.sessions.find((s) => s.id === this.activeSessionId);
    if (!session) return;

    const userMessageText = this.newMessage;

    if (session.messages.length === 1 && session.title === 'New Chat') {
      session.title =
        userMessageText.substring(0, 30) +
        (userMessageText.length > 30 ? '...' : '');
    }

    session.messages.push({
      content: userMessageText,
      role: 'user',
    });

    this.chatService.saveSessions(this.sessions);

    this.newMessage = '';
    this.isProcessing = true;
    this.isBlocked = false;

    // Clean history to only include role and content (Spring expects Map<String, String>)
    const historyToSend = session.messages.slice(0, -1).map((m) => ({
      role: m.role,
      content: m.content,
    }));

    this.chatService.ask(userMessageText, historyToSend).subscribe({
      next: (res: ChatResponse) => {
        let botReply = res.reply || 'No reply received from server.';
        let cType: any = null;
        let cLabels: string[] = [];
        let cSeries: any[] = [];

        // Extract the chart tag: [CHART: type]
        const chartMatch = botReply.match(/\[CHART:\s*(.*?)\]/i);
        let suggestedType = null;
        if (chartMatch) {
          suggestedType = chartMatch[1].toLowerCase().trim();
          botReply = botReply.replace(chartMatch[0], '').trim();
        }

        // Map the data if hasChart is true
        if (res.hasChart && res.chartData && suggestedType) {
          try {
            const data = res.chartData.data;
            const columns = res.chartData.columns;

            // Find the best columns for charting:
            // 1. Value index: find the first column that consistently has numbers
            // 2. Label index: find the first column that is NOT the value index
            let valueIndex = -1;
            let labelIndex = -1;

            if (data.length > 0) {
              // Look through columns to find one that is numeric
              for (let i = 0; i < data[0].length; i++) {
                if (
                  typeof data[0][i] === 'number' ||
                  (!isNaN(Number(data[0][i])) &&
                    String(data[0][i]).trim() !== '')
                ) {
                  valueIndex = i;
                  break;
                }
              }

              // Find a label column (prefer string/non-numeric)
              for (let i = 0; i < data[0].length; i++) {
                if (i !== valueIndex) {
                  labelIndex = i;
                  if (typeof data[0][i] === 'string') break; // Best case: first string column
                }
              }
            }

            if (valueIndex !== -1) {
              cType = suggestedType;
              cLabels = data.map((row: any[]) =>
                String(row[labelIndex !== -1 ? labelIndex : 0]),
              );
              const rawSeries = data.map((row: any[]) =>
                Number(row[valueIndex]),
              );

              if (cType === 'bar' || cType === 'line') {
                cSeries = [{ name: 'Analytics', data: rawSeries }];
              } else {
                cSeries = rawSeries;
              }
            }
          } catch (err) {
            console.error('Error mapping chart data', err);
            cType = null; // Revert if mapping fails
          }
        }

        session.messages.push({
          content: botReply,
          role: 'ai',
          chartType: cType,
          chartLabels: cLabels,
          chartSeries: cSeries,
        });

        if (res.blocked) this.isBlocked = true;
        this.isProcessing = false;
        this.chatService.saveSessions(this.sessions);
      },
      error: (err) => {
        console.error('Chat error', err);
        session.messages.push({
          content:
            'Sorry, I encountered an error and cannot respond at the moment.',
          role: 'ai',
        });
        this.isProcessing = false;
        this.chatService.saveSessions(this.sessions);
      },
    });
  }
}
