package com.mattcom.demostoreapp.product.category;


import com.mattcom.demostoreapp.requestmodels.ProductCategoryRequest;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProductCategory> createCategory(@RequestBody ProductCategoryRequest productCategoryRequest) {
        return ResponseEntity.ok(productCategoryService.addProductCategory(productCategoryRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<ProductCategory> updateCategory(@RequestBody ProductCategoryRequest productCategoryRequest) {
        return ResponseEntity.ok(productCategoryService.updateProductCategory(productCategoryRequest));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable("id") Integer id) {
        productCategoryService.deleteProductCategory(id);
    }





}
