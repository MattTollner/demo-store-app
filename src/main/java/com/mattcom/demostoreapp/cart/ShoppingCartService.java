package com.mattcom.demostoreapp.cart;

import com.mattcom.demostoreapp.cart.reqres.ShoppingCartQuantityRequest;
import com.mattcom.demostoreapp.product.Product;
import com.mattcom.demostoreapp.product.ProductRepository;
import com.mattcom.demostoreapp.product.exception.ProductNotFoundException;
import com.mattcom.demostoreapp.user.StoreUser;
import com.mattcom.demostoreapp.user.StoreUserRepository;
import com.mattcom.demostoreapp.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ShoppingCartService {


    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final StoreUserRepository storeUserRepository;
    private final ShoppingCartQuantitiesRepository shoppingCartQuantitiesRepository;

    public ShoppingCartService(ProductRepository productRepository, ShoppingCartRepository shoppingCartRepository, StoreUserRepository storeUserRepository, ShoppingCartQuantitiesRepository shoppingCartQuantitiesRepository) {
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.storeUserRepository = storeUserRepository;
        this.shoppingCartQuantitiesRepository = shoppingCartQuantitiesRepository;
    }

    public ShoppingCart getCart(StoreUser user) {
        Optional<ShoppingCart> cart = shoppingCartRepository.findByUser_Id(user.getId());
        return cart.orElseGet(() -> createCartForUser(user.getId()));
    }

    public void clearCart(Integer id) {
        Optional<ShoppingCart> cartOptional = shoppingCartRepository.findByUser_Id(id);
        if (cartOptional.isEmpty()) {
            //Todo handle exception
            return;
        }

        ShoppingCart cart = cartOptional.get();
        cart.setShoppingCartQuantities(new ArrayList<>());
        shoppingCartRepository.save(cart);
    }




    public void updateProductInCart(ShoppingCartQuantityRequest cartQuantityRequest, StoreUser user) {
        Optional<ShoppingCartQuantities> cartQuantityOpt = getShoppingCartQuantity(cartQuantityRequest, user);
        Optional<Product> product = productRepository.findById(cartQuantityRequest.getProductId());
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product with id " + cartQuantityRequest.getProductId() + " not found");
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
        if (cartQuantityOpt.isPresent() && cartQuantityRequest.getQuantity() == 0) {
            shoppingCartQuantity.setQuantity(0);
        } else {
            cartQuantityOpt.ifPresent(cartQuantities -> shoppingCartQuantity.setQuantity(cartQuantities.getQuantity() + cartQuantityRequest.getQuantity()));
        }
        return shoppingCartQuantity;
    }


    private ShoppingCart getShoppingCart(StoreUser user) throws UserNotFoundException {
        Optional<ShoppingCart> cartOpt = shoppingCartRepository.findByUser_Id(user.getId());

        return cartOpt.orElseGet(() -> createCartForUser(user.getId()));
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
            throw new UserNotFoundException("User not found");
        }

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user.get());
        return shoppingCartRepository.save(cart);
    }
}
