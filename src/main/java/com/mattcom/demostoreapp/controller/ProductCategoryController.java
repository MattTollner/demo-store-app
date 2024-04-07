package com.mattcom.demostoreapp.controller;


import com.mattcom.demostoreapp.requestmodels.ProductCategoryRequest;
import com.mattcom.demostoreapp.requestmodels.RoleRequest;
import com.mattcom.demostoreapp.service.ProductCategoryService;
import com.mattcom.demostoreapp.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class ProductCategoryController {


     ProductCategoryService productCategoryService;


    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void createRole(@RequestBody ProductCategoryRequest productCategoryRequest) throws Exception {
        productCategoryService.addProductCategory(productCategoryRequest);
    }

    @PutMapping("/update")
    public void updateRole(@RequestBody ProductCategoryRequest productCategoryRequest) throws Exception {
        productCategoryService.updateProductCategory(productCategoryRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable("id") Integer id) throws Exception {
        productCategoryService.deleteProductCategory(id);
    }





}
