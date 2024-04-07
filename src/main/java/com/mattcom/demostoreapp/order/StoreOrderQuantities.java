package com.mattcom.demostoreapp.order;

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
@Table(name = "store_order_quantities")
public class StoreOrderQuantities extends DefaultEntity {


    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;





    //Were never going to need the json of the order from the quantity so we can ignore it, this will prevent a cyclical fetch
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "store_order_id", nullable = false)
    private StoreOrder storeOrder;

    @Column(name = "price", nullable = false)
    private Double price;

    public StoreOrderQuantities(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }


}