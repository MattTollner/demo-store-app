//package com.mattcom.demostoreapp.dao;
//
//import com.mattcom.demostoreapp.entity.StoreUser;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//
//import java.util.List;
//
//@RepositoryRestResource(path = "users")
//public interface UserRepository extends JpaRepository<StoreUser,Integer> {
//
////    @Query("select a from User a left join fetch a.roles")
////    List<User> getUsersAndRoles();
////
////    @Query("select a from User a left join fetch a.roles left join fetch a.addresses where a.id = ?1")
////    User getUserAndRolesById(int id);
//
//}
