package com.mattcom.demostoreapp.product;

import com.mattcom.demostoreapp.inventory.Inventory;
import com.mattcom.demostoreapp.inventory.InventoryRepository;
import com.mattcom.demostoreapp.product.category.ProductCategory;
import com.mattcom.demostoreapp.product.category.ProductCategoryRepository;
import com.mattcom.demostoreapp.requestmodels.ProductRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    ProductRepository productRepository;
    ProductCategoryRepository productCategoryRepository;
    JdbcTemplate jdbcTemplate;
    InventoryRepository inventoryRepository;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public List<Product> getAllProductsWithCategory(Integer categoryId) {
        if (categoryId == null) {
            return productRepository.findAll();
        }

        Optional<ProductCategory> productCategoryOpt = productCategoryRepository.findById(categoryId);
        if (productCategoryOpt.isEmpty()) {
            throw new Error("Category not found" + categoryId);
        }
        ProductCategory productCategory = productCategoryOpt.get();
        if (productCategory.getParentCategoryId() > 0) {
            return productRepository.findByProductCategory_Id(categoryId);
        }

        return productRepository.findByProductCategoryRecursive(categoryId);

    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new Error("Product not found");
        }
        return product.get();
    }


    public void addProduct(ProductRequest productRequest) {
        Product product = new Product();

        Optional<ProductCategory> parent = productCategoryRepository.findById(productRequest.getProductCategoryId());
        if (parent.isEmpty()) {
            throw new Error("Category not found");
        }

        product.setProductImages(productRequest.getProductImages());

        product.setProductCategory(parent.get());
        product.setProductName(productRequest.getProductName());
        product.setProductDescription(productRequest.getProductDescription());
        product.setPrice(productRequest.getPrice());
        Inventory inventory = productRequest.getInventory();
        inventory.setProduct(product);
        product.setInventory(inventory);


        //product.setStock(productRequest.getStock());
        productRepository.save(product);
    }

    public void deleteProduct(Integer productId) throws Exception {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new Exception("Product not found");
        }
        productRepository.delete(product.get());
    }

    public void updateProduct(ProductRequest productRequest) throws Exception {
        Optional<Product> productOpt = productRepository.findById(productRequest.getId());
        if (productOpt.isEmpty()) {
            throw new Exception("Product not found");
        }

        ProductCategory category = null;
        if (productRequest.getProductCategoryId() != -1) {
            Optional<ProductCategory> categoryOpt = productCategoryRepository.findById(productRequest.getProductCategoryId());
            if (categoryOpt.isEmpty()) {
                throw new Exception("Parent Category Not Found");
            }
            category = categoryOpt.get();
        }

        Product product = productOpt.get();
        product.setProductCategory(category);
        product.setPrice(productRequest.getPrice());
        product.setProductName(productRequest.getProductName());
        product.updateInventory(productRequest.getInventory());
        product.setProductDescription(productRequest.getProductDescription());
        product.updateProductImages(productRequest.getProductImages());

        productRepository.save(product);
    }


}
