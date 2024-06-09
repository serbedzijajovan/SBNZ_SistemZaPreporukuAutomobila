package com.ftn.sbnz.model.mapping;

import com.ftn.sbnz.model.DTO.CarDTO;
import com.ftn.sbnz.model.models.Car;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper( CarMapper.class );

    CarDTO carToCarDTO(Car car);
    List<CarDTO> carsToCarsDTO(List<Car> cars);

    Car carDTOtoCar(CarDTO carDTO);
    List<Car> carsDTOToCars(List<CarDTO> carsDTO);
}
