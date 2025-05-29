package com.mattcom.demostoreapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "users")
public interface StoreUserRepository extends JpaRepository<StoreUser,Integer> {



     @Query("select a from StoreUser a left join fetch a.addresses")
     List<StoreUser> getUsersRolesAndAddresses();

     @Query("select a from StoreUser a left join fetch a.addresses where a.id = ?1")
     StoreUser getUsersRolesAndAddressesById(int id);

     Optional<StoreUser> findByEmailIgnoreCase(String email);

     @Query("select a from StoreUser a join fetch a.roles where upper(a.email) =  upper(?1)")
     Optional<StoreUser> getUserAndRolesByEmailIgnoreCase(String email);



}
