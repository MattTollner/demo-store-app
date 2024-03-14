package com.mattcom.demostoreapp.dao;

import com.mattcom.demostoreapp.entity.StoreOrder;
import com.mattcom.demostoreapp.entity.StoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreOrderRepository extends JpaRepository<StoreOrder, Integer> {


    List<StoreOrder> findByUser(StoreUser user);
}
