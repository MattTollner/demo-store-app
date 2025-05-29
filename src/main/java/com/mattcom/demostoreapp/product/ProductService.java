package com.mattcom.demostoreapp.product;

import com.mattcom.demostoreapp.inventory.Inventory;
import com.mattcom.demostoreapp.inventory.InventoryRepository;
import com.mattcom.demostoreapp.product.category.ProductCategory;
import com.mattcom.demostoreapp.product.category.ProductCategoryRepository;
import com.mattcom.demostoreapp.product.exception.CategoryNotFoundException;
import com.mattcom.demostoreapp.product.exception.ProductNotFoundException;
import com.mattcom.demostoreapp.requestmodels.ProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    ProductRepository productRepository;
    ProductCategoryRepository productCategoryRepository;
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
            throw new CategoryNotFoundException(categoryId);
        }
        ProductCategory productCategory = productCategoryOpt.get();
        if (productCategory.getParentCategoryId() > 0) {
            return productRepository.findByProductCategory_Id(categoryId);
        }

        return productRepository.findByProductCategoryRecursive(categoryId);

    }

    public Product getProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new Error("Product not found");
        }
        return product.get();
    }


    public Product addProduct(ProductRequest productRequest) {
        Product product = new Product();

        Optional<ProductCategory> parent = productCategoryRepository.findById(productRequest.getProductCategoryId());
        if (parent.isEmpty()) {
            throw new CategoryNotFoundException(productRequest.getProductCategoryId());
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
        return productRepository.save(product);
    }

    public void deleteProduct(Integer productId)  {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new ProductNotFoundException(productId);
        }
        productRepository.delete(product.get());
    }

    public Product updateProduct(ProductRequest productRequest) {
        Optional<Product> productOpt = productRepository.findById(productRequest.getId());
        if (productOpt.isEmpty()) {
            throw new ProductNotFoundException(productRequest.getId());
        }

        ProductCategory category = null;
        if (productRequest.getProductCategoryId() != -1) {
            Optional<ProductCategory> categoryOpt = productCategoryRepository.findById(productRequest.getProductCategoryId());
            if (categoryOpt.isEmpty()) {
                throw new CategoryNotFoundException(productRequest.getProductCategoryId());
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

        return productRepository.save(product);
    }


}
