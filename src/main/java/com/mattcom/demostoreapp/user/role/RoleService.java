package com.mattcom.demostoreapp.user.role;

import com.mattcom.demostoreapp.requestmodels.RoleRequest;
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
