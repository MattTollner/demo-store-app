package com.mattcom.demostoreapp.requestmodels;

import com.mattcom.demostoreapp.entity.Image;

import java.util.ArrayList;
import java.util.List;

public class ProductRequest {

    private int id;

    private String productName;

    private String productDescription;

    private int productCategoryId;

    private float price;

    private int stock;

    private List<Image> images;

    public ProductRequest(int id, String productName, String productDescription, int productCategoryId, float price, int stock, List<Image> images) {
        this.id = id;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategoryId = productCategoryId;
        this.price = price;
        this.stock = stock;
        this.images = images;
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

    public int getStock() {
        return stock;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

