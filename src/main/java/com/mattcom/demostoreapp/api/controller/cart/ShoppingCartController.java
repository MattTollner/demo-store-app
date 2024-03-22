package com.mattcom.demostoreapp.api.controller.cart;


import com.mattcom.demostoreapp.api.model.ShoppingCartQuantityRequest;
import com.mattcom.demostoreapp.entity.Address;
import com.mattcom.demostoreapp.entity.ShoppingCart;
import com.mattcom.demostoreapp.entity.ShoppingCartQuantities;
import com.mattcom.demostoreapp.entity.StoreUser;
import com.mattcom.demostoreapp.exception.UserNotFoundException;
import com.mattcom.demostoreapp.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    ShoppingCartService cartService;

    public ShoppingCartController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<ShoppingCartQuantities> getCart(@AuthenticationPrincipal StoreUser user) throws UserNotFoundException {
        ShoppingCart cart = cartService.getCart(user);
        return cart.getShoppingCartQuantities();
    }

    @DeleteMapping()
    public void decrementProductInCart(@AuthenticationPrincipal StoreUser user, @RequestBody ShoppingCartQuantityRequest request) throws Exception {
        cartService.decrementProductInCart(request.getProductId(), user, request.getQuantity());
    }

    @PutMapping
    public void updateProductInCart(@AuthenticationPrincipal StoreUser user, @RequestBody ShoppingCartQuantityRequest request) throws Exception {
        cartService.updateProductInCart(request, user);
    }
}
