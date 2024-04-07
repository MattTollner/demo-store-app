package com.mattcom.demostoreapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "verification_token")
public class VerificationToken extends DefaultEntity {

    @Lob
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "created", nullable = false)
    private Timestamp created;

    @ManyToOne(optional = false)
    @JoinColumn(name = "store_user_id", nullable = false)
    private StoreUser storeUser;


}