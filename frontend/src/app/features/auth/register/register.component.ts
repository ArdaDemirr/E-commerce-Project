import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

// Cross-field validator: password ve confirmPassword eşleşmeli
function passwordMatchValidator(
  control: AbstractControl,
): ValidationErrors | null {
  const password = control.get('password')?.value;
  const confirmPassword = control.get('confirmPassword')?.value;
  if (password && confirmPassword && password !== confirmPassword) {
    control.get('confirmPassword')?.setErrors({ passwordMismatch: true });
    return { passwordMismatch: true };
  } else {
    const errors = control.get('confirmPassword')?.errors;
    if (errors) {
      delete errors['passwordMismatch'];
      control
        .get('confirmPassword')
        ?.setErrors(Object.keys(errors).length ? errors : null);
    }
    return null;
  }
}

@Component({
  standalone: false,
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  registerForm: FormGroup;
  selectedRole: 'INDIVIDUAL' | 'CORPORATE' = 'INDIVIDUAL';
  showPassword = false;
  showConfirmPassword = false;
  isLoading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
  ) {
    this.registerForm = this.fb.group(
      {
        name: ['', Validators.required],
        surname: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(8)]],
        confirmPassword: ['', Validators.required],
        gender: ['', Validators.required],
        storeName: [''], // Eklendi
      },
      { validators: passwordMatchValidator },
    );
  }

  get confirmPasswordError(): boolean {
    const ctrl = this.registerForm.get('confirmPassword');
    return !!(ctrl?.touched && ctrl?.errors?.['passwordMismatch']);
  }

  selectRole(role: 'INDIVIDUAL' | 'CORPORATE'): void {
    this.selectedRole = role;
    const storeNameCtrl = this.registerForm.get('storeName');

    if (role === 'CORPORATE') {
      storeNameCtrl?.setValidators([Validators.required]);
    } else {
      storeNameCtrl?.clearValidators();
      storeNameCtrl?.setValue('');
    }
    storeNameCtrl?.updateValueAndValidity();
  }

  onRegister(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    this.errorMessage = '';
    // confirmPassword'ü backend'e gönderme
    const { confirmPassword, ...formData } = this.registerForm.value;
    const payload = { ...formData, role: this.selectedRole };
    this.authService.register(payload).subscribe({
      next: () => this.router.navigate(['/auth/login']),
      error: (err) => {
        this.errorMessage =
          err?.error?.message || 'Kayıt başarısız, lütfen tekrar deneyin.';
        this.isLoading = false;
      },
    });
  }
}
