package by.project.turamyzba.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nick_name", unique = true)
    private String nickName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

//    @Column(name = "phone_number", unique = true)
//    private String phoneNumber;

    @Column(name = "creation_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "is_verified")
    private Boolean isVerified;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }
}