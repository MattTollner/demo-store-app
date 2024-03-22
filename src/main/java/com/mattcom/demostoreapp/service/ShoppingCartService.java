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
        if(cart.isEmpty()){
            return createCartForUser(user.getId());
        }
        return cart.get();
    }


    //Todo should get the user from the context not the id
    public void updateProductInCart(ShoppingCartQuantityRequest cartQuantityRequest, StoreUser user) throws ProductNotFoundException, UserNotFoundException {
        Optional<ShoppingCartQuantities> cartQuantity = shoppingCartQuantitiesRepository.findById(cartQuantityRequest.getId());
        if(cartQuantity.isPresent() && cartQuantityRequest.getQuantity() == 0){
            shoppingCartQuantitiesRepository.delete(cartQuantity.get());
            return;
        }
        Optional<Product> product = productRepository.findById(cartQuantityRequest.getProductId());
        if(product.isEmpty()){
            throw new ProductNotFoundException();
        }

        Optional<ShoppingCart> cartOpt = shoppingCartRepository.findByUser_Id(user.getId());

        ShoppingCart cart;
        if(cartOpt.isEmpty()){
            cart = createCartForUser(user.getId());
        } else {
            cart = cartOpt.get();
        }
        cart.updateShoppingCartProduct(product.get(), cartQuantityRequest.getQuantity());
        shoppingCartRepository.save(cart);
    }

    public void decrementProductInCart(Integer productId, StoreUser user, Integer quantity) throws ProductNotFoundException, UserNotFoundException {
        Optional<Product> product = productRepository.findById(productId);
        if(product.isEmpty()){
            throw new ProductNotFoundException();
        }

        Optional<ShoppingCart> cartOpt = shoppingCartRepository.findByUser_Id(user.getId());

        ShoppingCart cart;
        if(cartOpt.isEmpty()){
            cart = createCartForUser(user.getId());
        } else {
            cart = cartOpt.get();
        }
        cart.removeShoppingCartProduct(product.get(), quantity);
        shoppingCartRepository.save(cart);
    }



    public ShoppingCart createCartForUser(Integer userId) throws UserNotFoundException {
        Optional<StoreUser> user = storeUserRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user.get());
        return shoppingCartRepository.save(cart);
    }
}
