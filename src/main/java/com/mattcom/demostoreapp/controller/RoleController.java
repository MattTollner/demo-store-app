package com.mattcom.demostoreapp.controller;


import com.mattcom.demostoreapp.dao.AddressRepository;
import com.mattcom.demostoreapp.dao.RoleRepository;
import com.mattcom.demostoreapp.dao.StoreUserRepository;
import com.mattcom.demostoreapp.entity.Address;
import com.mattcom.demostoreapp.entity.StoreUser;
import com.mattcom.demostoreapp.requestmodels.RoleRequest;
import com.mattcom.demostoreapp.requestmodels.StoreUserRequest;
import com.mattcom.demostoreapp.service.RoleService;
import com.mattcom.demostoreapp.service.StoreUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {


     RoleService roleService;


    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    public void createRole(@RequestBody RoleRequest roleRequest) throws Exception {
        roleService.createRole(roleRequest);
    }

    @PutMapping("/update")
    public void updateRole(@RequestBody RoleRequest roleRequest) throws Exception {
        roleService.updateRole(roleRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable("id") Integer id) throws Exception {
        roleService.deleteRole(id);
    }





}
