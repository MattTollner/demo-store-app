package com.mattcom.demostoreapp.dao;

import com.mattcom.demostoreapp.entity.StoreUser;
import com.mattcom.demostoreapp.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    Optional<VerificationToken> findByToken(String token);

    void deleteByStoreUser(StoreUser storeUser);

}
