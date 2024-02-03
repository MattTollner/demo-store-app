package com.mattcom.demostoreapp.dao;


import com.mattcom.demostoreapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "roles")
public interface RoleRepository extends JpaRepository<Role,Integer> {

    //@Query("select a from Role a left join fetch a.users where a.id = ?1")
    //User getRoleAndUsersById(int id);
}
