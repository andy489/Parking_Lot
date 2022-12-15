package com.pros.parkinglot.config;

import com.pros.parkinglot.config.role.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Value("${parking.lot.user1}")
    private String USER1;

    @Value("${parking.lot.pass1}")
    private String PASS1;

    @Value("${parking.lot.user2}")
    private String USER2;

    @Value("${parking.lot.pass2}")
    private String PASS2;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()
                .disable()
                .cors()
                .and()
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .httpBasic();
        return httpSecurity.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user1 = User.builder()
                .username(USER1)
                .password(encoder().encode(PASS1))
                .roles(Role.DEV.toString(), Role.MANAGER.toString())
                .build();

        UserDetails user2 = User.builder()
                .username(USER2)
                .password(encoder().encode(PASS2))
                .roles(Role.INTERN.toString())
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}