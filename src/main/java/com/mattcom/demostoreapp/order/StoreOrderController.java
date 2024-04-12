package com.mattcom.demostoreapp.order;

import com.mattcom.demostoreapp.order.reqres.PaymentResponse;
import com.mattcom.demostoreapp.requestmodels.CreateOrderRequest;
import com.mattcom.demostoreapp.user.StoreUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class StoreOrderController {

    private StoreOrderService storeOrderService;

    public StoreOrderController(StoreOrderService storeOrderService) {
        this.storeOrderService = storeOrderService;
    }

    @GetMapping
    public List<StoreOrder> getOrders(@AuthenticationPrincipal StoreUser user) {
        return storeOrderService.getOrders(user);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<StoreOrder> getOrder(@AuthenticationPrincipal StoreUser user, @PathVariable int orderId) {
        return ResponseEntity.ok(storeOrderService.getOrder(user, orderId));
    }

    @GetMapping("/orders-between")
    public List<StoreOrder> getOrdersBetween(@RequestParam String orderDateStart, @RequestParam Optional<String> orderDateEnd) {
        if(orderDateStart == null) {
            return new ArrayList<>();
        }

        LocalDateTime orderDateStartLocal = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(orderDateStart)),
                ZoneId.of("UTC"));
        LocalDateTime orderDateEndLocal = orderDateEnd.map(s -> LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(s)), ZoneId.of("UTC"))).orElseGet(LocalDateTime::now);


        return storeOrderService.getOrdersBetween(orderDateStartLocal, orderDateEndLocal);
    }

    //@PreAuthorize("hasRole('ADMIN')")

    @GetMapping("/product/{productId}")
    public List<StoreOrder> getOrdersByProduct(@PathVariable int productId) {
        return storeOrderService.getOrdersByProduct(productId);
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createOrder(@AuthenticationPrincipal StoreUser user, @RequestBody CreateOrderRequest createOrderRequest) throws Exception {
        return ResponseEntity.ok(storeOrderService.createOrder(user, createOrderRequest));
    }

    //Todo add user auth

    @PostMapping("/{orderId}")
    public ResponseEntity completeOrder(@AuthenticationPrincipal StoreUser user, @PathVariable int orderId) {
        storeOrderService.completeOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity cancelOrder(@AuthenticationPrincipal StoreUser user, @PathVariable int orderId) {
        storeOrderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }


}
