package com.mattcom.demostoreapp.dao;

import com.mattcom.demostoreapp.entity.StoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "users")
public interface StoreUserRepository extends JpaRepository<StoreUser,Integer> {

     @Query("select a from StoreUser a left join fetch a.addresses")
     List<StoreUser> getUsersRolesAndAddresses();

     @Query("select a from StoreUser a left join fetch a.addresses where a.id = ?1")
     StoreUser getUsersRolesAndAddressesById(int id);


}
