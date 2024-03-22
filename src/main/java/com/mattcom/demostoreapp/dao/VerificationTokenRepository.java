package com.mattcom.demostoreapp.dao;

import com.mattcom.demostoreapp.entity.StoreUser;
import com.mattcom.demostoreapp.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    Optional<VerificationToken> findByToken(String token);






    List<VerificationToken> findByStoreUser_IdOrderByIdDesc(Integer id);

    long deleteByStoreUser(StoreUser storeUser);
}
