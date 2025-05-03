package ru.prokdo.webtaxifleet.service.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ru.prokdo.webtaxifleet.dto.DriverDTO;
import ru.prokdo.webtaxifleet.entity.DriverEntity;
import ru.prokdo.webtaxifleet.repository.DriverRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    @Autowired
    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public void add(DriverDTO driverDTO) {
        DriverEntity driver = new DriverEntity();
        driver.setFirstName(driverDTO.getFirstName());
        driver.setLastName(driverDTO.getLastName());
        driver.setSurname(driverDTO.getSurname());
        driver.setPhone(driverDTO.getPhone());
        driver.setBirthDate(driverDTO.getBirthDate());
        driver.setAddress(driverDTO.getAddress());
        driver.setHireDate(driverDTO.getHireDate());
        driver.setFree(driverDTO.isFree());
        driver.setLicenseNumber(driverDTO.getLicenseNumber());
        driverRepository.save(driver);
    }

    public boolean existsByLicenseNumber(String licenseNumber) {
        return driverRepository.existsByLicenseNumber(licenseNumber);
    }

    public boolean existsByPhone(String licenseNumber) {
        return driverRepository.existsByPhone(licenseNumber);
    }

    public boolean occupyDriver(Integer driverId) {
        Optional<DriverEntity> driverOptional = driverRepository.findById(driverId);
        if (driverOptional.isPresent() && driverOptional.get().isFree()) {
            DriverEntity driver = driverOptional.get();
            driver.setFree(false);
            driverRepository.save(driver);
            return true;
        }
        return false;
    }

    public boolean freeDriver(Integer driverId) {
        Optional<DriverEntity> carOptional = driverRepository.findById(driverId);
        if (carOptional.isPresent()) {
            DriverEntity driver = carOptional.get();
            driver.setFree(true);
            driverRepository.save(driver);
            return true;
        }
        return false;
    }

    public Optional<DriverDTO> findById(Integer id) {
        return driverRepository.findById(id).map(this::convertToDTO);
    }

    public Optional<DriverDTO> findByPhone(String phone) {
        return driverRepository.findByPhone(phone).map(this::convertToDTO);
    }

    public List<DriverDTO> findAll(String licenseNumber, Boolean isFree, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<DriverEntity> driverPage;

        if (licenseNumber != null && !licenseNumber.isEmpty() && isFree != null) {
            driverPage = driverRepository.findByLicenseNumberAndIsFree(licenseNumber, isFree, pageable);
        } else if (licenseNumber != null && !licenseNumber.isEmpty()) {
            driverPage = driverRepository.findByLicenseNumber(licenseNumber, pageable);
        } else if (isFree != null) {
            driverPage = driverRepository.findByIsFree(isFree, pageable);
        } else {
            driverPage = driverRepository.findAll(pageable);
        }

        return driverPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Integer id) {
        DriverEntity driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Водитель с таким ID не найден"));
        driverRepository.delete(driver);
    }

    private DriverDTO convertToDTO(DriverEntity driverEntity) {
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setId(driverEntity.getId());
        driverDTO.setFirstName(driverEntity.getFirstName());
        driverDTO.setLastName(driverEntity.getLastName());
        driverDTO.setSurname(driverEntity.getSurname());
        driverDTO.setPhone(driverEntity.getPhone());
        driverDTO.setBirthDate(driverEntity.getBirthDate());
        driverDTO.setAddress(driverEntity.getAddress());
        driverDTO.setHireDate(driverEntity.getHireDate());
        driverDTO.setFree(driverEntity.isFree());
        driverDTO.setLicenseNumber(driverEntity.getLicenseNumber());
        return driverDTO;
    }
}
