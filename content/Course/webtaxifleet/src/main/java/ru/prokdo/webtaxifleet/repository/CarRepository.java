package ru.prokdo.webtaxifleet.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.prokdo.webtaxifleet.entity.CarEntity;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Integer> {
    Optional<CarEntity> findByLicensePlate(String licensePlate);
    Optional<CarEntity> findByDriverId(Integer driverId);
    boolean existsByLicensePlate(String licensePlate);
    Page<CarEntity> findByLicensePlate(String licensePlate, Pageable pageable);
    Page<CarEntity> findByDriverIdIsNull(Pageable pageable);
    Page<CarEntity> findByDriverIdIsNotNull(Pageable pageable);
    Page<CarEntity> findByLicensePlateAndDriverIdIsNull(String licensePlate, Pageable pageable);
    Page<CarEntity> findByLicensePlateAndDriverIdIsNotNull(String licensePlate, Pageable pageable);
}
