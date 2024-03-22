package com.mattcom.demostoreapp.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private StoreUser user;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ShoppingCartQuantities> shoppingCartQuantities = new ArrayList<>();

    public List<ShoppingCartQuantities> getShoppingCartQuantities() {
        return shoppingCartQuantities;
    }

    public void setShoppingCartQuantities(List<ShoppingCartQuantities> shoppingCartQuantities) {
        this.shoppingCartQuantities = shoppingCartQuantities;
    }

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


    public StoreUser getUser() {
        return user;
    }

    public void setUser(StoreUser user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}