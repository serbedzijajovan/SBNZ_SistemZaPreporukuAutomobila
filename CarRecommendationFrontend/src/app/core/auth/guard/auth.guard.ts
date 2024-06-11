import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {UserService} from "../services/user.service";

export const authGuard: CanActivateFn = (
  route,
  state
) => {
  const userService = inject(UserService);
  const router = inject(Router);

  if (userService.isLoggedIn()) {
    return true;
  } else {
    return router.navigate(['/authentication']);
  }
};

export const loginRegisterGuard: CanActivateFn = (
  route,
  state
) => {
  const userService = inject(UserService);
  const router = inject(Router);

  if (userService.isLoggedIn()) {
    return router.navigate(['/home/recommend-cars']);
  } else {
    return true;
  }
};
