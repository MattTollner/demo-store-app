package com.mattcom.demostoreapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "store_order")
public class StoreOrder extends DefaultEntity{

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private StoreUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "storeOrder", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<StoreOrderQuantities> quantities = new LinkedHashSet<>();

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;


    //write a method to calculate total price
    public double calculateTotalPrice(){

        double total = 0;
        for(StoreOrderQuantities storeOrderQuantities : quantities){
            total += storeOrderQuantities.getQuantity() * storeOrderQuantities.getProduct().getPrice();
        }
        return total;
    }

    public void addCartQuantityToOrder(ShoppingCartQuantities shoppingCartQuantities){
        StoreOrderQuantities storeOrderQuantities = new StoreOrderQuantities();
        storeOrderQuantities.setProduct(shoppingCartQuantities.getProduct());
        storeOrderQuantities.setQuantity(shoppingCartQuantities.getQuantity());
        storeOrderQuantities.setPrice(shoppingCartQuantities.getProduct().getPrice());
        storeOrderQuantities.setStoreOrder(this);
        quantities.add(storeOrderQuantities);
    }


}