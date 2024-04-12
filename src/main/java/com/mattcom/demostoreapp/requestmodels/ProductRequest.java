package com.mattcom.demostoreapp.requestmodels;

import com.mattcom.demostoreapp.inventory.Inventory;
import com.mattcom.demostoreapp.product.category.ProductCategory;
import com.mattcom.demostoreapp.product.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductRequest {

    private int id;

    private String productName;

    private String productDescription;

    private ProductCategory productCategory;

    private int productCategoryId;

    private float price;

    private Inventory inventory;

    private List<Image> productImages;

    public ProductRequest(int id, String productName, String productDescription, ProductCategory productCategory, int productCategoryId, float price, Inventory inventory, List<Image> productImages) {
        this.id = id;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.productCategoryId = productCategoryId;
        this.price = price;
        this.inventory = inventory;
        this.productImages = productImages;
    }


}

