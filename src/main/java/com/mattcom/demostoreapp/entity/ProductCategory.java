package com.mattcom.demostoreapp.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="product_category")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne()
    @JoinColumn(name="parent_category_id")
    private ProductCategory parentCategory;

    @Column(name = "category_name")
    private String categoryName;

    public ProductCategory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductCategory getParentCategory() {
        return parentCategory;
    }

    public int getParentCategoryId() {
        if(parentCategory == null){
            return -1;
        }
        return parentCategory.getId();
    }

    public void setParentCategory(ProductCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ProductCategory(ProductCategory parentCategory, String categoryName) {
        this.parentCategory = parentCategory;
        this.categoryName = categoryName;
    }
}
