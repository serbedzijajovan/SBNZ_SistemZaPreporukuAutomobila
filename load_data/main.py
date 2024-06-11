import pandas as pd

import requests


def read_cars():
    # Read data without specifying dtype for potentially problematic columns
    df = pd.read_csv("Car Dataset 1945-2020.csv", delimiter=',')

    rename_map = {
        'Make': 'make',
        'Modle': 'model',  # Corrected potential typo 'Modle' to 'Model'
        'Year_from': 'yearFrom',
        'Year_to': 'yearTo',
        'Body_type': 'bodyType',
        'number_of_seats': 'numberOfSeats',
        'length_mm': 'lengthMm',
        'width_mm': 'widthMm',
        'height_mm': 'heightMm',
        'wheelbase_mm': 'wheelbaseMm',
        'curb_weight_kg': 'curbWeightKg',
        'engine_hp': 'engineHp',
        'engine_type': 'engineType',
        'drive_wheels': 'driveWheels',
        'transmission': 'transmission',
        'fuel_tank_capacity_l': 'fuelTankCapacityL',
        'mixed_fuel_consumption_per_100_km_l': 'mixedFuelConsumptionPer100KmL',
        'max_speed_km_per_h': 'maxSpeedKmPerH',
        'city_fuel_per_100km_l': 'cityFuelPer100KmL',
        'highway_fuel_per_100km_l': 'highwayFuelPer100KmL',
        'acceleration_0_100_km/h_s': 'acceleration0100KmHS'
    }

    # Select relevant columns and drop rows with missing values
    df = df[list(rename_map.keys())]
    df.dropna(inplace=True)

    # Display the count of missing values for each column
    # nan_count = df.isna().sum()
    # print(nan_count)

    # List of columns to fix by replacing commas with periods
    cols_to_fix = [
        'curb_weight_kg',
        'height_mm',
        'width_mm',
        'length_mm',
        'wheelbase_mm',
        'mixed_fuel_consumption_per_100_km_l',
        'city_fuel_per_100km_l',
        'highway_fuel_per_100km_l',
        'max_speed_km_per_h',
        'fuel_tank_capacity_l',
        'acceleration_0_100_km/h_s'
    ]

    # Replace commas with periods in specified columns
    for col in cols_to_fix:
        df[col] = df[col].astype(str).str.replace(',', '.')

    def parse_seats(seat_str):
        return seat_str.split(',')[0]

    df['number_of_seats'] = df['number_of_seats'].apply(parse_seats)
    df['engine_type'] = df['engine_type'].apply(parse_seats)

    # Rename columns according to rename_map
    df = df.rename(columns=rename_map)

    # Define data types for columns
    dtype_map = {
        'yearFrom': int,
        'yearTo': int,
        'lengthMm': float,
        'widthMm': float,
        'heightMm': float,
        'wheelbaseMm': float,
        'curbWeightKg': float,
        'engineHp': int,
        'fuelTankCapacityL': float,
        'mixedFuelConsumptionPer100KmL': float,
        'maxSpeedKmPerH': float,
        'cityFuelPer100KmL': float,
        'highwayFuelPer100KmL': float,
        'acceleration0100KmHS': float
    }

    # Convert column data types
    df = df.astype(dtype_map)

    # Convert DataFrame to list of dictionaries
    cars_list = df.to_dict('records')
    return cars_list


def post_cars(cars):
    url = 'http://localhost:8080/cars/populate'
    headers = {'Content-Type': 'application/json'}
    response = requests.post(url, json=cars, headers=headers)
    print(response)
    # print("Status Code:", response.status_code)
    # print("Response Body:", response.text)


if __name__ == '__main__':
    cars = read_cars()
    post_cars(cars)
