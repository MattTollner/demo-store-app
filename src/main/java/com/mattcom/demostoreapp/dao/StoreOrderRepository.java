package com.mattcom.demostoreapp.dao;

import com.mattcom.demostoreapp.entity.StoreOrder;
import com.mattcom.demostoreapp.entity.StoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StoreOrderRepository extends JpaRepository<StoreOrder, Integer> {


    List<StoreOrder> findByUser(StoreUser user);

    List<StoreOrder> findByQuantities_Product_Id(int id);


    List<StoreOrder> findByCreatedAtGreaterThan(LocalDateTime orderDate);

    List<StoreOrder> findByCreatedAtBetween(LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

}
