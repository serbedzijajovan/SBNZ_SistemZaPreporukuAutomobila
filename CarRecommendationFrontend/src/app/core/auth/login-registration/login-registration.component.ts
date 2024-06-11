import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MatCard, MatCardContent, MatCardTitle} from "@angular/material/card";
import {MatFormField} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {NgIf} from "@angular/common";
import {UserService} from "../services/user.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login-registration',
  standalone: true,
  imports: [
    MatButton,
    MatCard,
    MatCardContent,
    MatCardTitle,
    MatFormField,
    MatInput,
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './login-registration.component.html',
  styleUrl: './login-registration.component.css'
})
export class LoginRegistrationComponent {
  constructor(private userService: UserService,
              private snackBar: MatSnackBar,
              private router: Router) {
  }

  loginForm: FormGroup = new FormGroup({
    email: new FormControl(''),
    password: new FormControl(''),
  });

  registrationForm: FormGroup = new FormGroup({
    fullName: new FormControl(''),
    email: new FormControl(''),
    password: new FormControl(''),
  });

  login() {
    if (this.loginForm.valid) {
      this.userService.login(this.loginForm.value).subscribe(      response => {
          this.router.navigate(['/home/recommend-cars']);
        },
        error => {
          this.snackBar.open('Invalid email or password!', 'Ok', {duration: 5000, panelClass: ['red-snackbar']});
        });
    }
  }

  register() {
    if (this.registrationForm.valid) {
      this.userService.register(this.registrationForm.value).subscribe(response => {
        this.snackBar.open('Registration successful!', 'Ok', {duration: 5000, panelClass: ['green-snackbar']});

        this.registrationForm.reset();
        this.showLoginForm = true;
      });
    }
  }

  showLoginForm: boolean = true;

  swapForm() {
    this.showLoginForm = !this.showLoginForm;
  }
}
