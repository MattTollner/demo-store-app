package com.mattcom.demostoreapp.service;

import com.mattcom.demostoreapp.dao.StoreOrderRepository;
import com.mattcom.demostoreapp.entity.StoreOrder;
import com.mattcom.demostoreapp.entity.StoreUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreOrderService {
    private StoreOrderRepository storeOrderRepository;

    public StoreOrderService(StoreOrderRepository storeOrderRepository) {
        this.storeOrderRepository = storeOrderRepository;
    }

    public List<StoreOrder> getOrders(StoreUser user){
        return storeOrderRepository.findByUser(user);
    }
}
