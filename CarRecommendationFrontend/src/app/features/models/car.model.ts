export interface Car {
  id: number;
  make: string;
  model: string;
  yearFrom: number;
  yearTo: number;
  bodyType: string;
  numberOfSeats: number;
  lengthMm: number;
  widthMm: number;
  heightMm: number;
  wheelBaseMm : number;
  curbWeightKg: number;
  engineHp: number;
  engineType: string;
  driveWheels: string;
  transmission: string;
  fuelTankCapacityL: number;
  mixedFuelConsumptionPer100Kml: number;
  maxSpeedKmPerH: number;
  cityFuelPer100KmL: number;
  highwayFuelPer100KmL: number;
  accelerationFuelPer100KmL: number;
}
