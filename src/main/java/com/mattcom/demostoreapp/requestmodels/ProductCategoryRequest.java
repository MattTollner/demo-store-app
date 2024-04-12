package com.mattcom.demostoreapp.requestmodels;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductCategoryRequest {

    private int id;

    private String categoryName;

    private int parentCategoryId;



    public ProductCategoryRequest(int id, int parentCategoryId, String categoryName) {
        this.id = id;
        this.parentCategoryId = parentCategoryId;
        this.categoryName = categoryName;
    }

}
