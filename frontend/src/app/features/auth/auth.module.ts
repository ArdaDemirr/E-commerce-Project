import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { LucideAngularModule, Mail, Lock, Eye, EyeOff, Zap, LogIn } from 'lucide-angular';

import { AuthRoutingModule } from './auth-routing.module';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { Register } from './register/register';

@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent
  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    ReactiveFormsModule,
    LucideAngularModule.pick({ Mail, Lock, Eye, EyeOff, Zap, LogIn })
  ]
})
export class AuthModule { }
