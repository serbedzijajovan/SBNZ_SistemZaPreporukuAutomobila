import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Car} from "../models/car.model";
import {Observable} from "rxjs";
import {User} from "../../core/auth/user.model";
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class CarService {
  constructor(private http: HttpClient) {}

  getCarRecommendations(carPreferenceType: string): Observable<Car[]> {
    return this.http.get<Car[]>(`/cars/recommendations?carPreferenceType=${carPreferenceType}`);
  }

  filterCars(make: string): Observable<Car[]> {
    return this.http.get<Car[]>(`/cars/filter?make=${make}`);
  }

  likeCar(email: string, carId: string) {
    const headers = new HttpHeaders({'Content-Type': 'application/json'});
    const body = { email: email, carId: carId };
    return this.http.post('/users/like-car', body, {headers, responseType: 'text' as 'json'});
  }

  filterCarsCustom(make: string, model:string, hp: number): Observable<Car[]> {
    return this.http.get<Car[]>(`/cars/filter-custom?make=${make}&model=${model}&hp=${hp}`);
  }
}
