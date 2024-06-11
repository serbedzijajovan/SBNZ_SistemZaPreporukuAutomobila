import {Component, OnInit, ViewChild} from '@angular/core';
import {
  MatCell,
  MatCellDef, MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef, MatTable, MatTableDataSource
} from "@angular/material/table";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption} from "@angular/material/core";
import {MatPaginator} from "@angular/material/paginator";
import {MatSelect} from "@angular/material/select";
import {NgForOf} from "@angular/common";
import {Car} from "../models/car.model";
import {User} from "../../core/auth/user.model";
import {CarService} from "../services/car.service";
import {UserService} from "../../core/auth/services/user.service";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";

interface PreferenceType {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-filter-custom-cars',
  standalone: true,
  imports: [
    MatCell,
    MatCellDef,
    MatFormField,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatLabel,
    MatOption,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatSelect,
    MatTable,
    NgForOf,
    MatColumnDef,
    MatHeaderCellDef,
    FormsModule,
    MatButton,
    MatInput,
    ReactiveFormsModule
  ],
  templateUrl: './filter-custom-cars.component.html',
  styleUrl: './filter-custom-cars.component.css'
})
export class FilterCustomCarsComponent implements OnInit{
  displayedColumns: string[] = ['id', 'make', 'model', 'bodyType', 'engineHp', 'yearFrom'];

  carRecommendations: Car[] = [];
  dataSource = new MatTableDataSource(this.carRecommendations);
  dataSourceWithPageSize = new MatTableDataSource(this.carRecommendations);

  currentUser: User | null = null;

  constructor(private carService: CarService,
              private userService: UserService) {
    this.getRecommendations('Alfa Romeo');
  }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSourceWithPageSize.paginator = this.paginatorPageSize;
  }

  @ViewChild('paginator') paginator: MatPaginator | null = null;
  @ViewChild('paginatorPageSize') paginatorPageSize: MatPaginator | null = null;

  loginForm: FormGroup = new FormGroup({
    make: new FormControl(''),
    model: new FormControl(''),
    hp: new FormControl(''),
  });

  getRecommendations(preferenceType: string): void {
    this.carService.filterCars(preferenceType).subscribe(
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

  login() {
    if (this.loginForm.valid) {
      this.carService.filterCarsCustom(this.loginForm.value['make'], this.loginForm.value['model'], this.loginForm.value['hp']).subscribe(
        (data: Car[]) => {
          console.log(data);
          this.dataSource.data = data;
        },
        error => {
          console.error('Error fetching car recommendations', error);
        }
      );
    }
  }
}
