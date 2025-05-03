package ru.prokdo.webtaxifleet.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.prokdo.webtaxifleet.dto.UserDTO;
import ru.prokdo.webtaxifleet.repository.AdministratorRepository;
import ru.prokdo.webtaxifleet.repository.DriverRepository;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private AdministratorRepository adminRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        var admin = adminRepository.findByPhone(phone);
        if (admin.isPresent()) {
            return new UserDTO(admin.get());
        }

        var driver = driverRepository.findByPhone(phone);
        if (driver.isPresent()) {
            return new UserDTO(driver.get());
        }

        throw new UsernameNotFoundException("Phone number not found");
    }
}
