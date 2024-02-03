package com.mattcom.demostoreapp.service;

import com.mattcom.demostoreapp.dao.RoleRepository;
import com.mattcom.demostoreapp.dao.StoreUserRepository;
import com.mattcom.demostoreapp.entity.Role;
import com.mattcom.demostoreapp.entity.StoreUser;
import com.mattcom.demostoreapp.requestmodels.RoleRequest;
import com.mattcom.demostoreapp.requestmodels.StoreUserRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Service
public class RoleService {

    RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(RoleRequest roleRequest){
        Role role = new Role();
        role.setRoleName(roleRequest.getRoleName());
        return roleRepository.save(role);
    }

    public Role updateRole(RoleRequest roleRequest) throws Exception {
        Optional<Role> roleOpt = roleRepository.findById(roleRequest.getId());
        if (!roleOpt.isPresent()) {
            throw new Exception("Role not found");
        }
        Role role = roleOpt.get();
        role.setRoleName(roleRequest.getRoleName());

        return roleRepository.save(role);
    }

    public void deleteRole(Integer id) throws Exception {
        Optional<Role> roleOpt = roleRepository.findById(id);
        if (!roleOpt.isPresent()) {
            throw new Exception("Role not found");
        }
        roleRepository.delete(roleOpt.get());
    }

}
