package com.mattcom.demostoreapp.product.category;

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
        if (parent.isPresent()) {
            productCategory.setParentCategory(parent.get());
        }

        productCategory.setCategoryName(productCategoryRequest.getCategoryName());
        return  productCategoryRepository.save(productCategory);
    }

    public ProductCategory updateProductCategory(ProductCategoryRequest productCategoryRequest) throws Exception {
        Optional<ProductCategory> categoryOpt = productCategoryRepository.findById(productCategoryRequest.getId());
        if (!categoryOpt.isPresent()) {
            throw new Exception("Category not found");
        }

        ProductCategory parent = null;
        if (productCategoryRequest.getParentCategoryId() != -1) {
            Optional<ProductCategory> parentOpt = productCategoryRepository.findById(productCategoryRequest.getParentCategoryId());
            if (!parentOpt.isPresent()) {
                throw new Exception("Parent Category Not Found");
            }
            parent = parentOpt.get();
        }

        ProductCategory category = categoryOpt.get();
        category.setParentCategory(parent);
        category.setCategoryName(productCategoryRequest.getCategoryName());
        return productCategoryRepository.save(category);
    }

    public void deleteProductCategory(Integer categoryId) throws Exception {
        Optional<ProductCategory> category = productCategoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new Exception("Category not found");
        }
        productCategoryRepository.delete(category.get());
    }


}
