package com.mattcom.demostoreapp.cart;


import com.mattcom.demostoreapp.cart.reqres.ShoppingCartQuantityRequest;
import com.mattcom.demostoreapp.user.StoreUser;
import com.mattcom.demostoreapp.user.exception.UserNotLoggedInException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    ShoppingCartService cartService;

    public ShoppingCartController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCartQuantities>> getCart(@AuthenticationPrincipal StoreUser user) {
        if (user == null) {
            throw new UserNotLoggedInException("User not logged in");
        }
        ShoppingCart cart = cartService.getCart(user);
        return ResponseEntity.ok(cart.getShoppingCartQuantities());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProductInCart(@AuthenticationPrincipal StoreUser user, @RequestBody ShoppingCartQuantityRequest request) {
        if (user == null) {
            throw new UserNotLoggedInException("User not logged in");
        }
        cartService.updateProductInCart(request, user);
    }
}
