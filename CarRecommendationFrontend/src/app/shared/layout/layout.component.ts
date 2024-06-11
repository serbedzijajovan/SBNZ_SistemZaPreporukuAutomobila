import { Component } from '@angular/core';
import {Router, RouterLink, RouterOutlet} from "@angular/router";
import {UserService} from "../../core/auth/services/user.service";

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {

  constructor(private userService: UserService, private router: Router) {}

  logout() {
    this.userService.logout();
    this.router.navigate(['/authentication']);
  }
}
