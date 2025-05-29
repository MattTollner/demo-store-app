package com.mattcom.demostoreapp.product.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mattcom.demostoreapp.entity.DefaultEntity;
import com.mattcom.demostoreapp.product.Product;
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
@Table(name="image")
public class Image extends DefaultEntity {

    @Column(name="img_url")
    private String imgUrl;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

}
