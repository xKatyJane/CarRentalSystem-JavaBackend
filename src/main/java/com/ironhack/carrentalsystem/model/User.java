package com.ironhack.carrentalsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.carrentalsystem.model.enums.DrivingLicenseStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private int telephoneNumber;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Collection<Role> roles = new ArrayList<>();
    private String drivingLicenseNumber;
    @Enumerated(EnumType.STRING)
    private DrivingLicenseStatus licenseStatus = DrivingLicenseStatus.NOT_PROVIDED;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
