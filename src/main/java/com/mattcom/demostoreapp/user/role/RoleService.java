package com.mattcom.demostoreapp.user.role;

import com.mattcom.demostoreapp.requestmodels.RoleRequest;
import com.mattcom.demostoreapp.user.exception.RoleNotFoundException;
import org.springframework.stereotype.Service;

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

    public Role updateRole(RoleRequest roleRequest) {
        Optional<Role> roleOpt = roleRepository.findById(roleRequest.getId());
        if (roleOpt.isEmpty()) {
            throw new RoleNotFoundException("Role not found " + roleRequest.getRoleName());
        }
        Role role = roleOpt.get();
        role.setRoleName(roleRequest.getRoleName());

        return roleRepository.save(role);
    }

    public void deleteRole(Integer id)  {
        Optional<Role> roleOpt = roleRepository.findById(id);
        if (roleOpt.isEmpty()) {
            throw new RoleNotFoundException("Role not found with id " + id);
        }
        roleRepository.delete(roleOpt.get());
    }

}
