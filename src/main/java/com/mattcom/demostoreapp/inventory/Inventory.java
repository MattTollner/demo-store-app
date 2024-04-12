package com.mattcom.demostoreapp.inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mattcom.demostoreapp.entity.DefaultEntity;
import com.mattcom.demostoreapp.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


//Improves performance as inventory is not always needed
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "inventory")
public class Inventory extends DefaultEntity {

    @JsonIgnore
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(name = "stock", nullable = false)
    private Integer stock;


}