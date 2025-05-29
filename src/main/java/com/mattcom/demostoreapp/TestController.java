package com.mattcom.demostoreapp;

import com.mattcom.demostoreapp.product.Product;
import com.mattcom.demostoreapp.product.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hi")
public class TestController {

    ProductRepository productRepository;

    public TestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @GetMapping("/test1")
    public Product getProduct() {
        return productRepository.findById(1).get();
    }

    @GetMapping("/test2")
    public ResponseEntity<Product> getProductTest() {
        return ResponseEntity.ok(productRepository.findById(1).get());
    }
}
