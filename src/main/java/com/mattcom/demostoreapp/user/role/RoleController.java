package com.mattcom.demostoreapp.user.role;


import com.mattcom.demostoreapp.requestmodels.RoleRequest;
import org.springframework.web.bind.annotation.*;

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
