package ru.prokdo.webtaxifleet.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ru.prokdo.webtaxifleet.entity.AdministratorEntity;
import ru.prokdo.webtaxifleet.entity.DriverEntity;

public class UserDTO implements UserDetails {
    private final String phone;
    private final String passwordHash;
    private final List<GrantedAuthority> authorities;

    public UserDTO(AdministratorEntity admin) {
        this.phone = admin.getPhone();
        this.passwordHash = admin.getPasswordHash();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));
    }

    public UserDTO(DriverEntity driver) {
        this.phone = driver.getPhone();
        this.passwordHash = null;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_DRIVER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
