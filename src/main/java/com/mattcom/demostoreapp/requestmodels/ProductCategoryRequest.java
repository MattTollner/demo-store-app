package com.mattcom.demostoreapp.requestmodels;

public class ProductCategoryRequest {

    private int id;

    private String categoryName;

    private int parentCategoryId;



    public ProductCategoryRequest(int id, int parentCategoryId, String categoryName) {
        this.id = id;
        this.parentCategoryId = parentCategoryId;
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}
