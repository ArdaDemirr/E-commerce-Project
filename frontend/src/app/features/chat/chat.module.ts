import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule, Bot, Send } from 'lucide-angular';
import { ChatRoutingModule } from './chat-routing.module';
import { ChatboxComponent } from './chatbox/chatbox.component';

@NgModule({
  declarations: [
    ChatboxComponent
  ],
  imports: [
    CommonModule,
    ChatRoutingModule,
    FormsModule,
    LucideAngularModule.pick({ Bot, Send })
  ]
})
export class ChatModule { }
