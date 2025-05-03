package ru.prokdo.webtaxifleet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.prokdo.webtaxifleet.entity.AdministratorEntity;

@Repository
public interface AdministratorRepository extends JpaRepository<AdministratorEntity, Integer> {
    Optional<AdministratorEntity> findByPhone(String phone);
    Optional<AdministratorEntity> findByEmail(String email);

    boolean existsByPhone(String phone);
}
