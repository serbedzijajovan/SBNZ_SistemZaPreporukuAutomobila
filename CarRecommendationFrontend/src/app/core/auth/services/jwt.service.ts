import {Injectable} from "@angular/core";
import {User} from "../user.model";
import {jwtDecode} from "jwt-decode";


@Injectable({providedIn: "root"})
export class JwtService {
  getToken(): string {
    return window.localStorage["jwtToken"];
  }

  saveToken(token: string): void {
    window.localStorage["jwtToken"] = token;
  }

  destroyToken(): void {
    window.localStorage.removeItem("jwtToken");
  }

  getUserFromToken(token: string): User | null {
    try {
      const decodedToken = jwtDecode(token)
      if (decodedToken && typeof decodedToken === 'object' && decodedToken['sub']) {
        return {email: decodedToken['sub'], token: token};
      }
    } catch (error) {
      console.error('Error decoding JWT token:', error);
    }
    return null;
  }

}
