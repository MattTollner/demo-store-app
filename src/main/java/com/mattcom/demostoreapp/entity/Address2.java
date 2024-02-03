//package com.mattcom.demostoreapp.entity;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "address")
//@JsonIdentityInfo(scope = Address2.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
//public class Address2 {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @Column(name="address_line_1")
//    private String addressLine1;
//
//    @Column(name="address_line_2")
//    private String addressLine2;
//
//    @Column(name="city")
//    private String city;
//
//    @Column(name="postcode")
//    private String postCode;
//
//
//    @ManyToOne()
//    @JoinColumn(name="user_id")
//    @JsonBackReference(value="user-addresses")
//    private User user;
//
//    public Address2() {
//    }
//
//    public Address2(String addressLine1, String addressLine2, String city, String postCode, User user) {
//        this.addressLine1 = addressLine1;
//        this.addressLine2 = addressLine2;
//        this.city = city;
//        this.postCode = postCode;
//        this.user = user;
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
//    public String getAddressLine1() {
//        return addressLine1;
//    }
//
//    public void setAddressLine1(String addressLine1) {
//        this.addressLine1 = addressLine1;
//    }
//
//    public String getAddressLine2() {
//        return addressLine2;
//    }
//
//    public void setAddressLine2(String addressLine2) {
//        this.addressLine2 = addressLine2;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getPostCode() {
//        return postCode;
//    }
//
//    public void setPostCode(String postCode) {
//        this.postCode = postCode;
//    }
//
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//}
