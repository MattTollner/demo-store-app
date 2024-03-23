package com.mattcom.demostoreapp.service;

import com.mattcom.demostoreapp.api.model.ShoppingCartQuantityRequest;
import com.mattcom.demostoreapp.dao.ShoppingCartQuantitiesRepository;
import com.mattcom.demostoreapp.dao.ShoppingCartRepository;
import com.mattcom.demostoreapp.dao.ProductRepository;
import com.mattcom.demostoreapp.dao.StoreUserRepository;
import com.mattcom.demostoreapp.entity.*;
import com.mattcom.demostoreapp.exception.ProductNotFoundException;
import com.mattcom.demostoreapp.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShoppingCartService {


    private ProductRepository productRepository;
    private ShoppingCartRepository shoppingCartRepository;
    private StoreUserRepository storeUserRepository;
    private ShoppingCartQuantitiesRepository shoppingCartQuantitiesRepository;

    public ShoppingCartService(ProductRepository productRepository, ShoppingCartRepository shoppingCartRepository, StoreUserRepository storeUserRepository, ShoppingCartQuantitiesRepository shoppingCartQuantitiesRepository) {
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.storeUserRepository = storeUserRepository;
        this.shoppingCartQuantitiesRepository = shoppingCartQuantitiesRepository;
    }

    public ShoppingCart getCart(StoreUser user) throws UserNotFoundException {
        Optional<ShoppingCart> cart = shoppingCartRepository.findByUser_Id(user.getId());
        if (cart.isEmpty()) {
            return createCartForUser(user.getId());
        }
        return cart.get();
    }


    //Todo should get the user from the context not the id
    public void updateProductInCart(ShoppingCartQuantityRequest cartQuantityRequest, StoreUser user) throws ProductNotFoundException, UserNotFoundException {
        Optional<ShoppingCartQuantities> cartQuantityOpt = getShoppingCartQuantity(cartQuantityRequest, user);
        Optional<Product> product = productRepository.findById(cartQuantityRequest.getProductId());
        if (product.isEmpty()) {
            throw new ProductNotFoundException();
        }
        ShoppingCart cart = getShoppingCart(user);
        shoppingCartRepository.save(updateCartQuantity(cartQuantityOpt, cartQuantityRequest, cart, product.get()));
    }

    private ShoppingCart updateCartQuantity(Optional<ShoppingCartQuantities> cartQuantityOpt, ShoppingCartQuantityRequest cartQuantityRequest, ShoppingCart cart, Product product) {
        ShoppingCartQuantities updatedCartQuantity = createUpdatedCartQuantity(cartQuantityOpt, cartQuantityRequest, cart, product);
        if (updatedCartQuantity.getQuantity() <= 0) {
            if (cartQuantityOpt.isPresent()) {
                shoppingCartQuantitiesRepository.delete(updatedCartQuantity);
                cart.removeShoppingCartQuantity(updatedCartQuantity);
            }
            return cart;
        }
        updatedCartQuantity = shoppingCartQuantitiesRepository.save(updatedCartQuantity);
        if (cartQuantityOpt.isEmpty()) {
            cart.addShoppingCartQuantity(updatedCartQuantity);
        }
        return cart;
    }

    private ShoppingCartQuantities createUpdatedCartQuantity(Optional<ShoppingCartQuantities> cartQuantityOpt, ShoppingCartQuantityRequest cartQuantityRequest, ShoppingCart cart, Product product) {
        ShoppingCartQuantities shoppingCartQuantity = cartQuantityOpt.orElseGet(() -> new ShoppingCartQuantities(product, cartQuantityRequest.getQuantity(), cart));
        cartQuantityOpt.ifPresent(cartQuantities -> shoppingCartQuantity.setQuantity(cartQuantities.getQuantity() + cartQuantityRequest.getQuantity()));
        return shoppingCartQuantity;
    }


    private ShoppingCart getShoppingCart(StoreUser user) throws UserNotFoundException {
        Optional<ShoppingCart> cartOpt = shoppingCartRepository.findByUser_Id(user.getId());

        ShoppingCart cart;
        if (cartOpt.isEmpty()) {
            cart = createCartForUser(user.getId());
        } else {
            cart = cartOpt.get();
        }
        return cart;
    }

    private Optional<ShoppingCartQuantities> getShoppingCartQuantity(ShoppingCartQuantityRequest cartQuantityRequest, StoreUser user) {
        Optional<ShoppingCartQuantities> cartQuantity = shoppingCartQuantitiesRepository.findById(cartQuantityRequest.getId());
        if (cartQuantity.isEmpty()) {
            cartQuantity = shoppingCartQuantitiesRepository.findByProduct_IdAndShoppingCart_User_Id(cartQuantityRequest.getProductId(), user.getId());
        }

        return cartQuantity;
    }


    private ShoppingCart createCartForUser(Integer userId) throws UserNotFoundException {
        Optional<StoreUser> user = storeUserRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user.get());
        return shoppingCartRepository.save(cart);
    }
}
