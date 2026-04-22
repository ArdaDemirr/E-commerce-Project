import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule, Bot, Send } from 'lucide-angular';
import { ChatRoutingModule } from './chat-routing.module';
import { ChatboxComponent } from './chatbox/chatbox.component';
import { NgApexchartsModule } from 'ng-apexcharts';

@NgModule({
  declarations: [ChatboxComponent],
  imports: [
    CommonModule,
    ChatRoutingModule,
    FormsModule,
    NgApexchartsModule,
    LucideAngularModule.pick({ Bot, Send }),
  ],
})
export class ChatModule {}
