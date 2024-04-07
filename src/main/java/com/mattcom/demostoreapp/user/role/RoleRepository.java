package com.mattcom.demostoreapp.user.role;


import com.mattcom.demostoreapp.user.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "roles")
public interface RoleRepository extends JpaRepository<Role,Integer> {

    //@Query("select a from Role a left join fetch a.users where a.id = ?1")
    //User getRoleAndUsersById(int id);


    Optional<Role> findByRoleName(String roleName);
}
