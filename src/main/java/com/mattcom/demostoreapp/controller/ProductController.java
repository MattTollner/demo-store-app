package com.mattcom.demostoreapp.controller;


import com.mattcom.demostoreapp.dao.ProductRepository;
import com.mattcom.demostoreapp.entity.Product;
import com.mattcom.demostoreapp.requestmodels.ProductRequest;
import com.mattcom.demostoreapp.service.ProductCategoryService;
import com.mattcom.demostoreapp.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {


     ProductService productService;
     ProductCategoryService productCategoryService;
     ProductRepository productRepository;


    public ProductController(ProductService productService, ProductCategoryService productCategoryService, ProductRepository productRepository) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.productRepository = productRepository;
    }

    @PostMapping("/create")
    public void createProduct(@RequestBody ProductRequest productRequest) throws Exception {
        productService.addProduct(productRequest);
    }

    @PutMapping("/update")
    public void updateProduct(@RequestBody ProductRequest productRequest) throws Exception {
        productService.updateProduct(productRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable("id") Integer id) throws Exception {
        productService.deleteProduct(id);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false) Integer categoryId){
        List<Product> result = productService.getAllProductsWithCategory(categoryId);
        return ResponseEntity.ok(result);
    }









}
