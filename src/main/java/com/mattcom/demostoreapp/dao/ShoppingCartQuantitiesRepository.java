package com.mattcom.demostoreapp.dao;

import com.mattcom.demostoreapp.entity.ShoppingCart;
import com.mattcom.demostoreapp.entity.ShoppingCartQuantities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartQuantitiesRepository extends JpaRepository<ShoppingCartQuantities, Integer> {


    Optional<ShoppingCartQuantities> findByProduct_IdAndShoppingCart_User_Id(int productId, Integer userId);


    List<ShoppingCartQuantities> findByShoppingCart_User_Id(Integer userId);

}
