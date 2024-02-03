//package com.mattcom.demostoreapp.entity;
//
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//import java.util.Set;
//
//@Entity
//@Table(name="user")
//@JsonIdentityInfo(scope = User.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="id")
//    private int id;
//
//    @Column(name="first_name")
//    private String firstName;
//
//    @Column(name="last_name")
//    private String lastName;
//
//
//    @Column(name="email")
//    private String email;
//
//    @Column(name="phone_number")
//    private String phoneNumber;
//
//    @Column(name="create_time")
//    private LocalDate createTime;
//
//    @Column(name="password")
//    private String password;
//
//
//    @ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
//    @JoinTable(name="user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
//    //@JsonManagedReference(value = "user-roles")
//    private Set<Role> roles;
//
//    @OneToMany(mappedBy = "user",
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
//    private Set<Address2> address2s;
//
//    public User() {
//    }
//
//
//    public Set<Address2> getAddresses() {
//        return address2s;
//    }
//
//    public void setAddresses(Set<Address2> address2s) {
//        this.address2s = address2s;
//    }
//
//    public void removeRole(Role role) {
//        this.roles.remove(role);
//    }
//
//
//    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public LocalDate getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(LocalDate createTime) {
//        this.createTime = createTime;
//    }
//
//    public User(String firstName, String lastName, String email, String phoneNumber, LocalDate createTime) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//        this.createTime = createTime;
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", email='" + email + '\'' +
//                ", phoneNumber='" + phoneNumber + '\'' +
//                ", createTime=" + createTime +
//                ", password='" + password + '\'' +
//                ", roles=" + roles +
//                '}';
//    }
//}
