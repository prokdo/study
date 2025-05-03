package ru.prokdo.webtaxifleet.service.data;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ru.prokdo.webtaxifleet.dto.CarDTO;
import ru.prokdo.webtaxifleet.entity.CarEntity;
import ru.prokdo.webtaxifleet.repository.CarRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void add(CarDTO carDTO) {
        CarEntity car = new CarEntity();
        car.setMake(carDTO.getMake());
        car.setModel(carDTO.getModel());
        car.setLicensePlate(carDTO.getLicensePlate());
        car.setDriverId(carDTO.getDriverId());
        car.setRegistrationDate(carDTO.getRegistrationDate());
        carRepository.save(car);
    }

    public boolean existsById(Integer id) {
        return carRepository.existsById(id);
    }

    public boolean existsByLicensePlate(String licensePlate) {
        return carRepository.existsByLicensePlate(licensePlate);
    }

    public Optional<CarDTO> findById(Integer id) {
        return carRepository.findById(id).map(this::convertToDTO);
    }

    public Optional<CarDTO> findByDriverId(Integer driverId) {
        return carRepository.findByDriverId(driverId).map(this::convertToDTO);
    }

    public List<CarDTO> findAll(String licensePlate, Boolean isFree, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<CarEntity> carPage;

        if (licensePlate != null && !licensePlate.isEmpty() && isFree != null && isFree) {
            carPage = carRepository.findByLicensePlateAndDriverIdIsNull(licensePlate, pageable);
        } else if (licensePlate != null && !licensePlate.isEmpty() && isFree != null && !isFree) {
                carPage = carRepository.findByLicensePlateAndDriverIdIsNotNull(licensePlate, pageable);
        } else if (licensePlate != null && !licensePlate.isEmpty()) {
            carPage = carRepository.findByLicensePlate(licensePlate, pageable);
        } else if (isFree != null && isFree) {
            carPage = carRepository.findByDriverIdIsNull(pageable);
        } else if (isFree!= null && !isFree) {
            carPage = carRepository.findByDriverIdIsNotNull(pageable);
        } else {
            carPage = carRepository.findAll(pageable);
        }

        return carPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Integer id) {
        CarEntity car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автомобиль с таким ID не найден"));
        carRepository.delete(car);
    }

    public boolean occupyCar(Integer carId, Integer driverId) {
        Optional<CarEntity> carOptional = carRepository.findById(carId);
        if (carOptional.isPresent() && carOptional.get().getDriverId() == null) {
            CarEntity car = carOptional.get();
            car.setDriverId(driverId);
            carRepository.save(car);
            return true;
        }
        return false;
    }

    public boolean freeCar(Integer carId) {
        Optional<CarEntity> carOptional = carRepository.findById(carId);
        if (carOptional.isPresent()) {
            CarEntity car = carOptional.get();
            car.setDriverId(null);
            carRepository.save(car);
            return true;
        }
        return false;
    }

    private CarDTO convertToDTO(CarEntity carEntity) {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(carEntity.getId());
        carDTO.setMake(carEntity.getMake());
        carDTO.setModel(carEntity.getModel());
        carDTO.setLicensePlate(carEntity.getLicensePlate());
        carDTO.setDriverId(carEntity.getDriverId());
        carDTO.setRegistrationDate(carEntity.getRegistrationDate());
        return carDTO;
    }
}
