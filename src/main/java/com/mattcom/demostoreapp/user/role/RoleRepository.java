package com.mattcom.demostoreapp.user.role;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "roles")
public interface RoleRepository extends JpaRepository<Role,Integer> {

}
