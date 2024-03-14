package com.mattcom.demostoreapp.controller;


import com.mattcom.demostoreapp.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", allowCredentials = "false", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.DELETE })
public class AdminController {

  private S3Service s3Service;




    @Autowired
    public AdminController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/gets3")
    public String getS3(){
        return s3Service.generateUploadUrl();
    }


    /*@PostMapping("/add/product")
    public void addProduct(@RequestBody ProductRequest productRequest) throws Exception {
        adminService.addProduct(productRequest);
    }

    @PutMapping("/update/product")
    public void updateProduct(@RequestBody ProductRequest productRequest) throws Exception {
        adminService.updateProduct(productRequest);
    }

    @DeleteMapping("/delete/product/{id}")
    public void deleteProduct(@PathVariable("id") Integer id) throws Exception {
        adminService.deleteProduct(id);
    }

    @PostMapping("/add/user")
    public void addUser(@RequestBody UserRequest userRequest) throws Exception {
        adminService.addUser(userRequest);
    }

    @PutMapping("/update/user")
    public void updateUser(@RequestBody UserRequest userRequest) throws Exception {
        adminService.updateUser(userRequest);
    }

    @DeleteMapping("/delete/user/{id}")
    public void deleteUser(@PathVariable("id") Integer id) throws Exception {
        adminService.deleteUser(id);
    }

    @PostMapping("/add/role")
    public void addUser(@RequestBody RoleRequest roleRequest) throws Exception {
        adminService.addRole(roleRequest);
    }

    @PutMapping("/update/role")
    public void updateRole(@RequestBody RoleRequest roleRequest) throws Exception {
        adminService.updateRole(roleRequest);
    }

    @DeleteMapping("/delete/role/{id}")
    public void deleteRole(@PathVariable("id") Integer id) throws Exception {
        adminService.deleteRole(id);
    }


    @PostMapping("/add/product-category")
    public void addProductCategory(@RequestBody ProductCategoryRequest productCategoryRequest) throws Exception {
        adminService.addProductCategory(productCategoryRequest);
    }



    @DeleteMapping("/delete/product-category")
    public void deleteProductCategory(@RequestParam Integer categoryId) throws Exception {
        adminService.deleteProductCategory(categoryId);
    }

    @PutMapping("/update/product-category")
    public void updateProductCategory(@RequestBody ProductCategoryRequest productCategoryRequest) throws Exception {
        adminService.updateProductCategory(productCategoryRequest);
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        List<User> users = adminService.getUsersAndRoles();
        return users;
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable("id") Integer id){
        User user = adminService.getUserAndRolesById(id);
        return user;
    }*/






}
