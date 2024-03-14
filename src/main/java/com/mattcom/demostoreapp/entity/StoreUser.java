package com.mattcom.demostoreapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "store_user")
public class StoreUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

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

    @Column(name = "create_time")
    private LocalDate createTime;

    @OneToMany(mappedBy = "storeUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id DESC")
    private Set<VerificationToken> verificationTokens = new LinkedHashSet<>();

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    public Boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Set<VerificationToken> getVerificationTokens() {
        return verificationTokens;
    }

    public void setVerificationTokens(Set<VerificationToken> verificationTokens) {
        this.verificationTokens = verificationTokens;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}