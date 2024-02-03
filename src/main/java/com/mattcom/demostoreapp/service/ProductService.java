package com.mattcom.demostoreapp.service;

import com.mattcom.demostoreapp.dao.ProductCategoryRepository;
import com.mattcom.demostoreapp.dao.ProductRepository;
import com.mattcom.demostoreapp.entity.Product;
import com.mattcom.demostoreapp.entity.ProductCategory;
import com.mattcom.demostoreapp.requestmodels.ProductCategoryRequest;
import com.mattcom.demostoreapp.requestmodels.ProductRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    ProductRepository productRepository;
    ProductCategoryRepository productCategoryRepository;
    JdbcTemplate jdbcTemplate;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<Product>getAllProductsWithCategory(Integer categoryId){
        if(categoryId == null){
            return productRepository.findAll();
        }

        Optional<ProductCategory> productCategoryOpt = productCategoryRepository.findById(categoryId);
        if(!productCategoryOpt.isPresent()){
            throw new Error("Category not found" + categoryId);
        }
        ProductCategory productCategory = productCategoryOpt.get();
        if(productCategory.getParentCategoryId() > 0){
            return productRepository.findByProductCategory_Id(categoryId);
        }

        return productRepository.findByProductCategoryRecursive(categoryId);

    }

    public void addProduct(ProductRequest productRequest) {
        Product product = new Product();

        Optional<ProductCategory> parent = productCategoryRepository.findById(productRequest.getProductCategoryId());
        if(!parent.isPresent()){
            throw new Error("Category not found");
        }

        product.setImages(productRequest.getImages());

        product.setProductCategory(parent.get());
        product.setProductName(productRequest.getProductName());
        product.setProductDescription(productRequest.getProductDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        productRepository.save(product);
    }

        public void deleteProduct(Integer productId) throws Exception {
       Optional<Product> product = productRepository.findById(productId);
       if (!product.isPresent()) {
           throw new Exception("Product not found");
       }
       productRepository.delete(product.get());
   }

   public void updateProduct(ProductRequest productRequest) throws Exception {
       Optional<Product> productOpt = productRepository.findById(productRequest.getId());
       if (!productOpt.isPresent()) {
           throw new Exception("Product not found");
       }

       ProductCategory category = null;
       if(productRequest.getProductCategoryId() != -1){
           Optional<ProductCategory> categoryOpt = productCategoryRepository.findById(productRequest.getProductCategoryId());
           if(!categoryOpt.isPresent()){
               throw new Exception("Parent Category Not Found");
           }
           category = categoryOpt.get();
       }

       Product product = productOpt.get();
       product.setProductCategory(category);
       product.setPrice(productRequest.getPrice());
       product.setProductName(productRequest.getProductName());
       product.setStock(productRequest.getStock());
       product.setProductDescription(productRequest.getProductDescription());
       product.setImages(productRequest.getImages());
       productRepository.save(product);
   }


}
