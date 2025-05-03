package ru.prokdo.webtaxifleet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.prokdo.webtaxifleet.entity.TripEntity;

@Repository
public interface TripRepository extends JpaRepository<TripEntity, Integer>, JpaSpecificationExecutor<TripEntity> {
    List<TripEntity> findByDriverId(Integer driverId);
    List<TripEntity> findByCarId(Integer carId);
    boolean existsByDriverId(Integer driverId);
    boolean existsByCarId(Integer carId);
}
