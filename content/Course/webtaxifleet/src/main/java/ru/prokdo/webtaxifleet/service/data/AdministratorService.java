package ru.prokdo.webtaxifleet.service.data;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.prokdo.webtaxifleet.dto.AdministratorDTO;
import ru.prokdo.webtaxifleet.entity.AdministratorEntity;
import ru.prokdo.webtaxifleet.repository.AdministratorRepository;

@Service
public class AdministratorService {
    private final AdministratorRepository adminRepository;

    @Autowired
    public AdministratorService(AdministratorRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public boolean existsByPhone(String phone) {
        return adminRepository.existsByPhone(phone);
    }

    public Optional<AdministratorDTO> findByPhone(String phone) {
        return adminRepository.findByPhone(phone).map(this::convertToDTO);
    }

    private AdministratorDTO convertToDTO(AdministratorEntity adminEntity) {
        AdministratorDTO adminDTO = new AdministratorDTO();
        adminDTO.setId(adminEntity.getId());
        adminDTO.setFirstName(adminEntity.getFirstName());
        adminDTO.setLastName(adminEntity.getLastName());
        adminDTO.setSurname(adminEntity.getSurname());
        adminDTO.setPhone(adminEntity.getPhone());
        adminDTO.setEmail(adminEntity.getEmail());
        adminDTO.setPasswordHash(adminEntity.getPasswordHash());
        return adminDTO;
    }
}
