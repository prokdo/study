package ru.prokdo.webtaxifleet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import ru.prokdo.webtaxifleet.security.HashPasswordEncoder;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationProvider phoneNumberAuthenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String phoneNumber = authentication.getName();
                UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);

                return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public HashPasswordEncoder passwordEncoder() {
        return new HashPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/administrator/login").permitAll()
                .requestMatchers("/administrator/**").hasRole("ADMINISTRATOR")
                .requestMatchers("/driver/**").hasRole("DRIVER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/")
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll()
            )
            .authenticationProvider(phoneNumberAuthenticationProvider());

        return http.build();
    }
}
