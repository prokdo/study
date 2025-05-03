package ru.prokdo.webtaxifleet.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.prokdo.webtaxifleet.entity.DriverEntity;

@Repository
public interface DriverRepository extends JpaRepository<DriverEntity, Integer> {
    Optional<DriverEntity> findByPhone(String phone);
    Optional<DriverEntity> findByLicenseNumber(String licenseNumber);

    boolean existsByLicenseNumber(String licenseNumber);
    boolean existsByPhone(String phone);

    Page<DriverEntity> findByLicenseNumber(String licenseNumber, Pageable pageable);
    Page<DriverEntity> findByIsFree(Boolean isFree, Pageable pageable);
    Page<DriverEntity> findByLicenseNumberAndIsFree(String licenseNumber, Boolean isFree, Pageable pageable);
}
