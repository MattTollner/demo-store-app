package com.mattcom.demostoreapp.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mattcom.demostoreapp.user.address.Address;
import com.mattcom.demostoreapp.entity.DefaultEntity;
import com.mattcom.demostoreapp.user.role.Role;
import com.mattcom.demostoreapp.auth.token.VerificationToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "store_user")
public class StoreUser extends DefaultEntity implements UserDetails  {

    @Column(name = "password", nullable = false, length = 1000)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;



    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Address> addresses = new LinkedHashSet<>();

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;


    @JsonIgnore
    @OneToMany(mappedBy = "storeUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id DESC")
    private Set<VerificationToken> verificationTokens = new LinkedHashSet<>();



    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @ManyToMany
    @JoinTable(name = "store_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new LinkedHashSet<>();




    public Boolean isEmailVerified() {
        return emailVerified;
    }



    public String[] getRolesAsStringArray(){
        return roles.stream().map(Role::getRoleName).toArray(String[]::new);
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        roles.size();
        Collection<SimpleGrantedAuthority> result = roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).toList();
        return result;
    }

    @Override
    public String getUsername() {
        return this.email;
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