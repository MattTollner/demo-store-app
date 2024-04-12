package com.mattcom.demostoreapp.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {


    Optional<ShoppingCart> findByUser_Id(Integer id);

    void deleteByShoppingCartQuantities(ShoppingCartQuantities shoppingCartQuantities);

}
