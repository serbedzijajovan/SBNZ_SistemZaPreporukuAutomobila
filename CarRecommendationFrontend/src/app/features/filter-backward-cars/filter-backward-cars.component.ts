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
import {Car} from "../models/car.model";
import {CarService} from "../services/car.service";
import {NgForOf} from "@angular/common";
import {UserService} from "../../core/auth/services/user.service";
import {User} from "../../core/auth/user.model";

interface PreferenceType {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-filter-backward-cars',
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
    MatColumnDef,
    MatHeaderCellDef,
    NgForOf
  ],
  templateUrl: './filter-backward-cars.component.html',
  styleUrl: './filter-backward-cars.component.css'
})
export class FilterBackwardCarsComponent implements OnInit{
  displayedColumns: string[] = ['id', 'make', 'model', 'bodyType', 'engineHp', 'yearFrom'];

  preferenceTypes: PreferenceType[] = [
    {value: 'BMW', viewValue: 'BMW'},
    {value: 'Audi', viewValue: 'Audi'},
    {value: 'Alfa Romeo', viewValue: 'Alfa Romeo'},
  ];

  selectedPreferenceType: PreferenceType = {value: 'Alfa Romeo', viewValue: 'Alfa Romeo'};

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
}
