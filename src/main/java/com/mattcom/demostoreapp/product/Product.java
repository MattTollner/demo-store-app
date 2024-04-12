package com.mattcom.demostoreapp.product;

import com.mattcom.demostoreapp.entity.DefaultEntity;
import com.mattcom.demostoreapp.inventory.Inventory;
import com.mattcom.demostoreapp.product.category.ProductCategory;
import com.mattcom.demostoreapp.product.image.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "product")
public class Product extends DefaultEntity {

    @ManyToOne()
    @JoinColumn(name = "product_category")
    private ProductCategory productCategory;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "price", nullable = false)
    private double price;

    @OneToOne(mappedBy = "product", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, optional = false, orphanRemoval = true)
    private Inventory inventory;

    @Size(message = "Must be between 1 and 10 images", min = 1, max = 10)
    @NotNull
    @OneToMany(mappedBy = "product", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Image> productImages = new ArrayList<>();


    public void setProductImages(List<Image> productImages) {
        productImages.forEach(image -> image.setProduct(this));
        this.productImages = productImages;
    }

    public void updateProductImages(List<Image> newProductImages) {
        List<Image> imagesToRemove = this.productImages.stream()
                .filter(image -> newProductImages.stream().noneMatch(newImage -> newImage.getId().equals(image.getId())))
                .toList();
        List<Image> imagesToAdd = newProductImages.stream()
                .filter(newImage -> this.productImages.stream().noneMatch(image -> image.getId().equals(newImage.getId())))
                .toList();
        imagesToAdd.forEach(image -> image.setProduct(this));

        this.productImages.removeAll(imagesToRemove);
        this.productImages.addAll(imagesToAdd);
    }


    public void setInventory(Inventory inventory) {
        inventory.setProduct(this);
        this.inventory = inventory;
    }

    public void updateInventory(Inventory inventory){
        this.inventory.setStock(inventory.getStock());
    }


    public int getProductCategoryId() {
        if (productCategory == null) {
            return -1;
        }
        return productCategory.getId();
    }

    public void addImage(Image image) {
        if (this.productImages == null) {
            this.productImages = new ArrayList<>();
        }
        this.productImages.add(image);
    }

}
