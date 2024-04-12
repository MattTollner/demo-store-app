package com.mattcom.demostoreapp.cart.reqres;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShoppingCartQuantityRequest {

    private Integer quantity;
    private Integer productId;
    private Integer id;

}
