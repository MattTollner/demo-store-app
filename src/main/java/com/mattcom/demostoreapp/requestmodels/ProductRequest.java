package com.mattcom.demostoreapp.requestmodels;

import com.mattcom.demostoreapp.product.image.Image;
import com.mattcom.demostoreapp.inventory.Inventory;
import com.mattcom.demostoreapp.product.category.ProductCategory;

import java.util.List;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }



    public List<Image> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<Image> productImages) {
        this.productImages = productImages;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}

