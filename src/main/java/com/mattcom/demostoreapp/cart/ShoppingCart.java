package com.mattcom.demostoreapp.cart;

import com.mattcom.demostoreapp.entity.DefaultEntity;
import com.mattcom.demostoreapp.user.StoreUser;
import jakarta.persistence.*;
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
@Table(name = "shopping_cart")
public class ShoppingCart extends DefaultEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private StoreUser user;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ShoppingCartQuantities> shoppingCartQuantities = new ArrayList<>();


    public void removeShoppingCartQuantity(ShoppingCartQuantities shoppingCartQuantities) {
        this.shoppingCartQuantities.removeIf(cartQuantity -> cartQuantity.getId().equals(shoppingCartQuantities.getId()));
    }

    public void addShoppingCartQuantity(ShoppingCartQuantities shoppingCartQuantities) {
        this.shoppingCartQuantities.add(shoppingCartQuantities);
    }
}