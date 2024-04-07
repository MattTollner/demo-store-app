package com.mattcom.demostoreapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart extends DefaultEntity{

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private StoreUser user;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ShoppingCartQuantities> shoppingCartQuantities = new ArrayList<>();

    public void updateShoppingCartProduct(Product product, Integer quantity){
        ShoppingCartQuantities existingProduct = getProductFromId(product.getId());
        if(existingProduct != null){
            existingProduct.setQuantity(quantity);
            return;
        }
        if(quantity == 0){
            return;
        }
        ShoppingCartQuantities shoppingCartQuantities = new ShoppingCartQuantities();
        shoppingCartQuantities.setProduct(product);
        shoppingCartQuantities.setQuantity(quantity);
        shoppingCartQuantities.setShoppingCart(this);
        this.shoppingCartQuantities.add(shoppingCartQuantities);
    }

    public void removeShoppingCartQuantity(ShoppingCartQuantities shoppingCartQuantities){
        this.shoppingCartQuantities.removeIf(cartQuantity -> cartQuantity.getId().equals(shoppingCartQuantities.getId()));
    }

    public void removeShoppingCartProduct(Product product, Integer quantity){
        ShoppingCartQuantities existingProduct = getProductFromId(product.getId());
        if(existingProduct == null){
            return;
        }
        existingProduct.setQuantity(quantity);
        if(existingProduct.getQuantity() == 0){
            this.shoppingCartQuantities.remove(existingProduct);
        }
    }

    private ShoppingCartQuantities getProductFromId(Integer id){
        for(ShoppingCartQuantities shoppingCartItem : shoppingCartQuantities){
            if(shoppingCartItem.getProduct().getId() == id){
                return shoppingCartItem;
            }
        }
        return null;
    }


    public void addShoppingCartQuantity(ShoppingCartQuantities shoppingCartQuantities) {
        this.shoppingCartQuantities.add(shoppingCartQuantities);
    }
}