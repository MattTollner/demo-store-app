package com.mattcom.demostoreapp.product.category;

import com.mattcom.demostoreapp.product.exception.CategoryNotFoundException;
import com.mattcom.demostoreapp.requestmodels.ProductCategoryRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ProductCategoryService {

    ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public ProductCategory addProductCategory(ProductCategoryRequest productCategoryRequest) {
        ProductCategory productCategory = new ProductCategory();

        Optional<ProductCategory> parent = productCategoryRepository.findById(productCategoryRequest.getParentCategoryId());
        parent.ifPresent(productCategory::setParentCategory);

        productCategory.setCategoryName(productCategoryRequest.getCategoryName());
        return  productCategoryRepository.save(productCategory);
    }

    public ProductCategory updateProductCategory(ProductCategoryRequest productCategoryRequest) {
        Optional<ProductCategory> categoryOpt = productCategoryRepository.findById(productCategoryRequest.getId());
        if (categoryOpt.isEmpty()) {
            throw new CategoryNotFoundException("Category not found with id " + productCategoryRequest.getId());
        }

        ProductCategory parent = null;
        if (productCategoryRequest.getParentCategoryId() != -1) {
            Optional<ProductCategory> parentOpt = productCategoryRepository.findById(productCategoryRequest.getParentCategoryId());
            if (parentOpt.isEmpty()) {
                throw new CategoryNotFoundException("Parent Category Not Found with id " + productCategoryRequest.getId());
            }
            parent = parentOpt.get();
        }

        ProductCategory category = categoryOpt.get();
        category.setParentCategory(parent);
        category.setCategoryName(productCategoryRequest.getCategoryName());
        return productCategoryRepository.save(category);
    }

    public void deleteProductCategory(Integer categoryId) {
        Optional<ProductCategory> category = productCategoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new CategoryNotFoundException("Category not found with id " + categoryId);
        }
        productCategoryRepository.delete(category.get());
    }


}
