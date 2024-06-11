import { Routes } from '@angular/router';
import {authGuard, loginRegisterGuard} from "./core/auth/guard/auth.guard";

export const routes: Routes = [
  {
    path: "",
    redirectTo: "/authentication",
    pathMatch: "full",
  },
  {
    path: "authentication",
    loadComponent: () =>
      import("./core/auth/login-registration/login-registration.component").then(m => m.LoginRegistrationComponent),
    canActivate: [loginRegisterGuard],
  },
  {
    path: "home",
    loadComponent: () =>
      import("./shared/layout/layout.component").then(m => m.LayoutComponent),
    canActivate: [authGuard],
    children: [
      {
        path: "recommend-cars",
        loadComponent: () =>
          import("./features/recommend-cars/recommend-cars.component").then(m => m.RecommendCarsComponent),
      },
      {
        path: "filter-backward",
        loadComponent: () =>
          import("./features/filter-backward-cars/filter-backward-cars.component").then(m => m.FilterBackwardCarsComponent),
      },
      {
        path: "filter-custom",
        loadComponent: () =>
          import("./features/filter-custom-cars/filter-custom-cars.component").then(m => m.FilterCustomCarsComponent),
      },
    ],
  },
];
