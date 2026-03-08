import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ChatboxComponent } from './chatbox/chatbox.component';

const routes: Routes = [
  { path: '', component: ChatboxComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ChatRoutingModule { }
