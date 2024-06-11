import {Component, OnInit, ViewChild} from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable, MatTableDataSource
} from "@angular/material/table";
import {NgForOf} from "@angular/common";
import {MatPaginator} from "@angular/material/paginator";
import {Car} from "../models/car.model";
import {CarService} from "../services/car.service";
import {MatFormField, MatFormFieldModule} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {FormsModule} from "@angular/forms";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatSnackBar} from "@angular/material/snack-bar";
import {User} from "../../core/auth/user.model";
import {UserService} from "../../core/auth/services/user.service";

interface PreferenceType {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-recommend-cars',
  standalone: true,
  imports: [
    MatTable,
    MatColumnDef,
    NgForOf,
    MatHeaderCell,
    MatHeaderCellDef,
    MatCell,
    MatCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatPaginator,
    MatFormField,
    MatSelect,
    MatOption,
    MatFormFieldModule,
    FormsModule,
    MatIcon,
    MatIconButton
  ],
  templateUrl: './recommend-cars.component.html',
  styleUrl: './recommend-cars.component.css'
})
export class RecommendCarsComponent implements OnInit {
  displayedColumns: string[] = ['id', 'make', 'model', 'bodyType', 'engineHp', 'yearFrom'];
  columnsToDisplay: string[] = ['id', 'make', 'model', 'bodyType', 'engineHp', 'yearFrom', 'like'];

  preferenceTypes: PreferenceType[] = [
    {value: 'SPORT', viewValue: 'Sport'},
    {value: 'FAMILY_FRIENDLY', viewValue: 'Family Friendly'},
    {value: 'OFF_ROAD', viewValue: 'Off Road'},
  ];

  selectedPreferenceType: PreferenceType = {value: 'SPORT', viewValue: 'Sport'};

  carRecommendations: Car[] = [];
  dataSource = new MatTableDataSource(this.carRecommendations);
  dataSourceWithPageSize = new MatTableDataSource(this.carRecommendations);

  constructor(private carService: CarService,
              private snackBar: MatSnackBar,
              private userService: UserService) {
    this.getRecommendations('SPORT');
  }

  currentUser: User | null = null;

  ngOnInit(): void {
    this.userService.currentUser.subscribe(data => {
      this.currentUser = data;
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSourceWithPageSize.paginator = this.paginatorPageSize;
  }

  @ViewChild('paginator') paginator: MatPaginator | null = null;
  @ViewChild('paginatorPageSize') paginatorPageSize: MatPaginator | null = null;

  getRecommendations(preferenceType: string): void {
    this.carService.getCarRecommendations(preferenceType).subscribe(
      (data: Car[]) => {
        this.dataSource.data = data;
      },
      error => {
        console.error('Error fetching car recommendations', error);
      }
    );
  }

  preferenceSelected(value: string) {
    this.getRecommendations(value);
  }

  onActionClick(carID: string) {
    if (this.currentUser) {
      this.carService.likeCar(this.currentUser.email, carID).subscribe(
        () => {
          this.snackBar.open('Registration successful!', 'Ok', {duration: 5000, panelClass: ['green-snackbar']});
        },
        error => {
          this.snackBar.open('You are banned from liking!', 'Ok', {duration: 5000, panelClass: ['red-snackbar']});
        }
      );
    } else {
      window.alert("User is null.")
    }
  }
}
