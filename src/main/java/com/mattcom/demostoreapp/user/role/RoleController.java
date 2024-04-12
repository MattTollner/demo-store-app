package com.mattcom.demostoreapp.user.role;


import com.mattcom.demostoreapp.requestmodels.RoleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {


     RoleService roleService;


    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody RoleRequest roleRequest) {
        return ResponseEntity.ok(roleService.createRole(roleRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<Role> updateRole(@RequestBody RoleRequest roleRequest)  {
        return ResponseEntity.ok(roleService.updateRole(roleRequest));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable("id") Integer id)  {
        roleService.deleteRole(id);
    }





}
