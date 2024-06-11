import {Injectable} from "@angular/core";
import {Observable, BehaviorSubject} from "rxjs";

import {JwtService} from "./jwt.service";
import {map, distinctUntilChanged, tap} from "rxjs/operators";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../user.model";
import {Router} from "@angular/router";

@Injectable({providedIn: "root"})
export class UserService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser = this.currentUserSubject
    .asObservable()
    .pipe(distinctUntilChanged());

  public isAuthenticated = this.currentUser.pipe(map((user) => !!user));

  constructor(
    private readonly http: HttpClient,
    private readonly jwtService: JwtService,
    private readonly router: Router,
  ) {
    this.loadUser();
  }

  private loadUser(): void {
    const token = this.jwtService.getToken();
    if (token) {
      const user = this.jwtService.getUserFromToken(token);
      if (user) {
        this.setCurrentUser(user);
      } else {
        this.purgeAuth();
      }
    } else {
      this.purgeAuth();
    }
  }

  login(credentials: {
    email: string;
    password: string;
  }): Observable<User> {
    return this.http
      .post<User>("/auth/login", credentials)
      .pipe(tap(user => this.loginUser(user)));
  }

  register(credentials: {
    fullName: string;
    email: string;
    password: string;
  }): Observable<any> {
    const headers = new HttpHeaders({'Content-Type': 'application/json'});
    return this.http.post("/auth/register", credentials, {headers, responseType: 'text' as 'json'}).pipe();
  }

  logout(): void {
    this.purgeAuth();
    void this.router.navigate(["/"]);
  }

  update(user: Partial<User>): Observable<{ user: User }> {
    return this.http.put<{ user: User }>("/user", {user}).pipe(
      tap(({user}) => {
        this.currentUserSubject.next(user);
      }),
    );
  }

  loginUser(user: User): void {
    this.jwtService.saveToken(user.token);
    this.currentUserSubject.next(user);
  }

  setCurrentUser(user: User) {
    this.currentUserSubject.next(user);
  }

  purgeAuth(): void {
    this.jwtService.destroyToken();
    this.currentUserSubject.next(null);
  }

  isLoggedIn(): boolean {
    return !!this.currentUserSubject.value;
  }
}
