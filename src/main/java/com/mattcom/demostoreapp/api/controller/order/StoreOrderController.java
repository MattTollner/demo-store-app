package com.mattcom.demostoreapp.api.controller.order;

import com.mattcom.demostoreapp.entity.StoreOrder;
import com.mattcom.demostoreapp.entity.StoreUser;
import com.mattcom.demostoreapp.service.StoreOrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class StoreOrderController {

    private StoreOrderService storeOrderService;

    public StoreOrderController(StoreOrderService storeOrderService) {
        this.storeOrderService = storeOrderService;
    }

    @GetMapping
    public List<StoreOrder> getOrders(@AuthenticationPrincipal StoreUser user){
        return storeOrderService.getOrders(user);
    }



}
