package com.mattcom.demostoreapp.cart;

import static org.junit.jupiter.api.Assertions.*;

import com.mattcom.demostoreapp.user.StoreUser;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ShoppingCartControllerTest {
    @Mock
    private StoreUser mockUser;

    @Mock
    private ShoppingCartService mockCartService;

    @InjectMocks
    private ShoppingCartController shoppingCartController;

    @Test
    public void testGetCart_UserIsNull_ReturnsUnauthorized() {
        ResponseEntity<List<ShoppingCartQuantities>> response = shoppingCartController.getCart(null);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testGetCart_UserIsNotNull_ReturnsCartQuantities() {
        ShoppingCart mockCart = new ShoppingCart(); // Create a mock ShoppingCart object
        Mockito.when(mockCartService.getCart(mockUser)).thenReturn(mockCart);

        ResponseEntity<List<ShoppingCartQuantities>> response = shoppingCartController.getCart(mockUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCart.getShoppingCartQuantities(), response.getBody());
    }
}
