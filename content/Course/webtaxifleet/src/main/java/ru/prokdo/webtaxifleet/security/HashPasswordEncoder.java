package ru.prokdo.webtaxifleet.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class HashPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return String.valueOf(rawPassword.hashCode());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
