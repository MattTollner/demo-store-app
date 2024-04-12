package com.mattcom.demostoreapp.cart;

import com.mattcom.demostoreapp.cart.reqres.ShoppingCartQuantityRequest;
import com.mattcom.demostoreapp.product.Product;
import com.mattcom.demostoreapp.product.ProductRepository;
import com.mattcom.demostoreapp.product.exception.ProductNotFoundException;
import com.mattcom.demostoreapp.user.StoreUser;
import com.mattcom.demostoreapp.user.StoreUserRepository;
import com.mattcom.demostoreapp.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ShoppingCartServiceTest {

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private StoreUserRepository storeUserRepository;
    @Mock
    private ShoppingCartQuantitiesRepository shoppingCartQuantitiesRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testUpdateProductInCart_ProductNotFound_ShouldThrowProductNotFoundException() {
//        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());
//        assertThrows(ProductNotFoundException.class, () -> shoppingCartService.updateProductInCart(cartQuantityRequest, user));
//    }

    @Test
    public void testUpdateProductInCart_ProductFound_ExistingCart_ShouldCallSave() {
        StoreUser user = new StoreUser();
        user.setId(1);
        Product product = new Product();
        product.setId(1);
        ShoppingCartQuantityRequest cartQuantityRequest = new ShoppingCartQuantityRequest();
        cartQuantityRequest.setId(1);
        cartQuantityRequest.setQuantity(1);
        cartQuantityRequest.setProductId(1);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        ShoppingCartQuantities shoppingCartQuantities = new ShoppingCartQuantities(product, 2, shoppingCart);
        shoppingCart.getShoppingCartQuantities().add(shoppingCartQuantities);
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(shoppingCartQuantitiesRepository.findByProduct_IdAndShoppingCart_User_Id(anyInt(), anyInt())).thenReturn(Optional.of(shoppingCartQuantities));
        when(storeUserRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUser_Id(anyInt())).thenReturn(Optional.of(shoppingCart));
        shoppingCartService.updateProductInCart(cartQuantityRequest, user);
        ArgumentCaptor<ShoppingCart> argument = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartRepository, times(1)).save(any());
        assertEquals(3, shoppingCart.getShoppingCartQuantities().getFirst().getQuantity());
    }

    @Test
    public void testUpdateProductInCart_ProductFound_CreateCart_ShouldCallSave() {
        StoreUser user = new StoreUser();
        user.setId(1);
        Product product = new Product();
        product.setId(1);
        ShoppingCartQuantityRequest cartQuantityRequest = new ShoppingCartQuantityRequest();
        cartQuantityRequest.setId(1);
        cartQuantityRequest.setQuantity(2);
        cartQuantityRequest.setProductId(1);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(storeUserRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUser_Id(anyInt())).thenReturn(Optional.empty());
        doReturn(shoppingCart).when(shoppingCartRepository).save(any(ShoppingCart.class));
        shoppingCartService.updateProductInCart(cartQuantityRequest, user);

        verify(shoppingCartRepository, times(2)).save(any());
        assertEquals(1, shoppingCart.getShoppingCartQuantities().size(), "Shopping cart quantities should have 1");
    }

    @Test
    public void testUpdateProductInCart_DecrementProduct_ExistingCart_ShouldCallSave() {
        StoreUser user = new StoreUser();
        user.setId(1);
        Product product = new Product();
        product.setId(1);
        ShoppingCartQuantityRequest cartQuantityRequest = new ShoppingCartQuantityRequest();
        cartQuantityRequest.setId(1);
        cartQuantityRequest.setQuantity(-1);
        cartQuantityRequest.setProductId(1);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        ShoppingCartQuantities shoppingCartQuantities = new ShoppingCartQuantities(product, 2, shoppingCart);
        shoppingCart.getShoppingCartQuantities().add(shoppingCartQuantities);
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(shoppingCartQuantitiesRepository.findByProduct_IdAndShoppingCart_User_Id(anyInt(), anyInt())).thenReturn(Optional.of(shoppingCartQuantities));
        when(storeUserRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUser_Id(anyInt())).thenReturn(Optional.of(shoppingCart));
        shoppingCartService.updateProductInCart(cartQuantityRequest, user);
        ArgumentCaptor<ShoppingCart> argument = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartRepository, times(1)).save(any());
        assertEquals(1, shoppingCart.getShoppingCartQuantities().getFirst().getQuantity());
    }

    @Test
    public void testUpdateProductInCart_DecrementAndRemoveProduct_ExistingCart_ShouldCallSave() {
        StoreUser user = new StoreUser();
        user.setId(1);
        Product product = new Product();
        product.setId(1);
        ShoppingCartQuantityRequest cartQuantityRequest = new ShoppingCartQuantityRequest();
        cartQuantityRequest.setId(1);
        cartQuantityRequest.setQuantity(-1);
        cartQuantityRequest.setProductId(1);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        ShoppingCartQuantities shoppingCartQuantities = new ShoppingCartQuantities(product, 1, shoppingCart);
        shoppingCartQuantities.setId(1);
        shoppingCart.getShoppingCartQuantities().add(shoppingCartQuantities);
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(shoppingCartQuantitiesRepository.findByProduct_IdAndShoppingCart_User_Id(anyInt(), anyInt())).thenReturn(Optional.of(shoppingCartQuantities));
        when(storeUserRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUser_Id(anyInt())).thenReturn(Optional.of(shoppingCart));
        shoppingCartService.updateProductInCart(cartQuantityRequest, user);
        ArgumentCaptor<ShoppingCart> argument = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartRepository, times(1)).save(any());
        assertTrue(shoppingCart.getShoppingCartQuantities().isEmpty());
    }

    @Test
    public void testUpdateProductInCart_IncrementAndAddProduct_ExistingCart_ShouldCallSave() {
        StoreUser user = new StoreUser();
        user.setId(1);
        Product product = new Product();
        product.setId(1);
        ShoppingCartQuantityRequest cartQuantityRequest = new ShoppingCartQuantityRequest();
        cartQuantityRequest.setId(1);
        cartQuantityRequest.setQuantity(2);
        cartQuantityRequest.setProductId(1);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        ShoppingCartQuantities shoppingCartQuantities = new ShoppingCartQuantities(product, cartQuantityRequest.getQuantity(), shoppingCart);
        shoppingCartQuantities.setId(1);
        //shoppingCart.getShoppingCartQuantities().add(shoppingCartQuantities);

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(shoppingCartQuantitiesRepository.findByProduct_IdAndShoppingCart_User_Id(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(shoppingCartQuantitiesRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(storeUserRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUser_Id(anyInt())).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartQuantitiesRepository.save(any())).thenReturn(shoppingCartQuantities);

        shoppingCartService.updateProductInCart(cartQuantityRequest, user);

        verify(shoppingCartRepository, times(1)).save(any());
        assertEquals(2, shoppingCart.getShoppingCartQuantities().getFirst().getQuantity());
    }

    @Test
    public void testUpdateProductInCart_ProductNotFound_ShouldThrowProductNotFoundException() {
        StoreUser user = new StoreUser();
        user.setId(1);
        Product product = new Product();
        product.setId(1);
        ShoppingCartQuantityRequest cartQuantityRequest = new ShoppingCartQuantityRequest();
        cartQuantityRequest.setId(1);
        cartQuantityRequest.setQuantity(2);
        cartQuantityRequest.setProductId(1);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.getShoppingCartQuantities().add(new ShoppingCartQuantities(product, 1, shoppingCart));
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(storeUserRepository.findById(anyInt())).thenReturn(Optional.of(user));
        assertThrows(ProductNotFoundException.class, () -> shoppingCartService.updateProductInCart(cartQuantityRequest, user));

    }

    @Test
    public void testClearCart_NonEmptyCart_ShouldClearCart() {
        ShoppingCart cart = new ShoppingCart();
        ShoppingCartQuantities shoppingCartQuantities = new ShoppingCartQuantities();
        shoppingCartQuantities.setShoppingCart(cart);
        cart.getShoppingCartQuantities().add(shoppingCartQuantities);

        when(shoppingCartRepository.findByUser_Id(anyInt())).thenReturn(Optional.of(cart));
        ShoppingCart savedCart = new ShoppingCart();


        shoppingCartService.clearCart(1);
        ArgumentCaptor<ShoppingCart> cartCaptor = ArgumentCaptor.forClass(ShoppingCart.class);
        assertTrue(cart.getShoppingCartQuantities().isEmpty());
        verify(shoppingCartRepository, times(1)).save(cart);
        assertTrue(cart.getShoppingCartQuantities().isEmpty(), "Shopping cart quantities should be empty");
    }

    @Test
    public void testClearCart_EmptyCart_ShouldNotClearCart() {
        when(shoppingCartRepository.findByUser_Id(anyInt())).thenReturn(Optional.empty());
        shoppingCartService.clearCart(1);
        verify(shoppingCartRepository, never()).save(any());
    }





    @Test
    public void testGetCartWhenCartExists() {
        StoreUser user = new StoreUser();
        user.setId(33);
        ShoppingCart existingCart = new ShoppingCart();
        existingCart.setUser(user);

        when(shoppingCartRepository.findByUser_Id(anyInt())).thenReturn(Optional.of(existingCart));

        ShoppingCart result = shoppingCartService.getCart(user);

        assertNotNull(result);
        assertEquals(existingCart, result);
    }



    @Test
    public void testGetCartWhenCartDoesNotExist() {
        StoreUser user = new StoreUser();
        user.setId(33);

        // Stub the behavior of storeUserRepository.findById to return the mock storeUser
        when(storeUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        // Create a mock shoppingCart
        ShoppingCart mockCart = new ShoppingCart();
        mockCart.setUser(user);
        // Stub the behavior of shoppingCartRepository.save to return the mock cart when createCartForUser is called
        doReturn(mockCart).when(shoppingCartRepository).save(any(ShoppingCart.class));

        // Stub the behavior of shoppingCartRepository.findByUser_Id to return an empty Optional
        when(shoppingCartRepository.findByUser_Id(anyInt())).thenReturn(Optional.empty());

        // Call the getCart method
        ShoppingCart result = shoppingCartService.getCart(user);

        // Assert that the result is not null
        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    public void testCreateCartForUser_ExistingUser_ShouldCreateCart() {
        int userId = 1;
        StoreUser existingUser = new StoreUser();
        existingUser.setId(userId);
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUser(existingUser);



        when(storeUserRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(newCart);
        ShoppingCart createdCart = shoppingCartService.getCart(existingUser);

        assertNotNull(createdCart);
        assertEquals(existingUser, createdCart.getUser());
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testCreateCartForUser_NonExistingUser_ShouldThrowUserNotFoundException() {
        StoreUser newUser = new StoreUser();
        newUser.setId(1);

        when(storeUserRepository.findById(newUser.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            shoppingCartService.getCart(newUser);
        });

        verify(shoppingCartRepository, never()).save(any(ShoppingCart.class));
    }

}