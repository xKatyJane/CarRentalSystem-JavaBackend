package com.ironhack.carrentalsystem.security;

import com.ironhack.carrentalsystem.security.filters.CustomAuthenticationFilter;
import com.ironhack.carrentalsystem.security.filters.CustomAuthorizationFilter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.DELETE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
//        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBuilder.getOrBuild());
//    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
//        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests

                        // Open endpoint
                        .requestMatchers("/api/login/**", "/api/cars", "/api/cars/priceRange/**", "/api/cars/available/**").permitAll()

                        // Registration only for unauthenticated users
                        .requestMatchers(POST, "/api/users/registration/**").anonymous()

                        // Authenticated endpoints
                        .requestMatchers(GET, "/api/users/me/**", "/api/bookings/myBookings").authenticated()
                        .requestMatchers(PATCH, "/api/users/me/**").authenticated()

                        // Admin endpoints
                        .requestMatchers(GET, "api/users", "/api/bookings").hasAnyAuthority("ADMIN")
                        .requestMatchers(POST, "api/cars/**", "/api/users/newUser/**", "/api/bookings/newBooking/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(PATCH, "api/cars", "/api/bookings/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(DELETE, "api/cars/**", "/api/users/**").hasAnyAuthority("ADMIN")

                        .anyRequest().authenticated());

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }
}

