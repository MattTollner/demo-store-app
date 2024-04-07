package com.mattcom.demostoreapp.product;


import com.mattcom.demostoreapp.requestmodels.ProductRequest;
import com.mattcom.demostoreapp.product.category.ProductCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id){
        return ResponseEntity.ok(productService.getProduct(id));
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
