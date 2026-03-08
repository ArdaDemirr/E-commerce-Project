import {
    Component,
    OnInit,
    ViewChild,
    ElementRef,
    AfterViewChecked,
    ChangeDetectorRef,
} from '@angular/core';
import { ChatService, ChatResponse } from '../../../core/services/chat.service';
import { Title } from '@angular/platform-browser';

interface Message {
    text: string;
    sender: 'user' | 'bot';
    timestamp: Date;
}

@Component({
    standalone: false,
    selector: 'app-chatbox',
    templateUrl: './chatbox.component.html',
    styleUrls: ['./chatbox.component.scss'],
})
export class ChatboxComponent implements OnInit, AfterViewChecked {
    messages: Message[] = [];
    newMessage: string = '';
    isProcessing: boolean = false;
    isBlocked: boolean = false;

    @ViewChild('scrollMe') private myScrollContainer!: ElementRef;

    constructor(
        private chatService: ChatService,
        private titleService: Title,
        private cdr: ChangeDetectorRef,
    ) { }

    ngOnInit(): void {
        this.titleService.setTitle('DataPulse | Support Chat');
        this.messages.push({
            text: 'Hello! How can I help you today?',
            sender: 'bot',
            timestamp: new Date(),
        });
    }

    ngAfterViewChecked() {
        this.scrollToBottom();
    }

    scrollToBottom(): void {
        try {
            this.myScrollContainer.nativeElement.scrollTop =
                this.myScrollContainer.nativeElement.scrollHeight;
        } catch (err) { }
    }

    sendMessage() {
        if (!this.newMessage.trim() || this.isProcessing) return;

        const userMessageText = this.newMessage;
        this.messages.push({
            text: userMessageText,
            sender: 'user',
            timestamp: new Date(),
        });

        this.newMessage = '';
        this.isProcessing = true;
        this.isBlocked = false;

        this.chatService.ask(userMessageText).subscribe({
            next: (res: ChatResponse) => {
                this.messages.push({
                    text: res.reply || 'No reply received from server.',
                    sender: 'bot',
                    timestamp: new Date(),
                });
                if (res.blocked) this.isBlocked = true;
                this.isProcessing = false;
                this.cdr.detectChanges();
            },
            error: (err) => {
                console.error('Chat error', err);
                this.messages.push({
                    text: 'Sorry, I encountered an error and cannot respond at the moment.',
                    sender: 'bot',
                    timestamp: new Date(),
                });
                this.isProcessing = false;
                this.cdr.detectChanges();
            },
        });
    }
}