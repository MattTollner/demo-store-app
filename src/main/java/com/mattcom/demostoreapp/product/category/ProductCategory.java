package com.mattcom.demostoreapp.product.category;

import com.mattcom.demostoreapp.entity.DefaultEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="product_category")
public class ProductCategory extends DefaultEntity {

    @ManyToOne()
    @JoinColumn(name="parent_category_id")
    private ProductCategory parentCategory;

    @Column(name = "category_name")
    private String categoryName;


    public int getParentCategoryId() {
        if(parentCategory == null){
            return -1;
        }
        return parentCategory.getId();
    }

}
