package com.mattcom.demostoreapp.cart;


import com.mattcom.demostoreapp.cart.reqres.ShoppingCartQuantityRequest;
import com.mattcom.demostoreapp.user.StoreUser;
import com.mattcom.demostoreapp.user.exception.UserNotFoundException;
import com.mattcom.demostoreapp.user.exception.UserNotLoggedInException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    ShoppingCartService cartService;

    public ShoppingCartController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCartQuantities>> getCart(@AuthenticationPrincipal StoreUser user)  {
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            ShoppingCart cart = cartService.getCart(user);
            return ResponseEntity.ok(cart.getShoppingCartQuantities());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping
    public void updateProductInCart(@AuthenticationPrincipal StoreUser user, @RequestBody ShoppingCartQuantityRequest request) throws Exception {
        cartService.updateProductInCart(request, user);
    }
}
