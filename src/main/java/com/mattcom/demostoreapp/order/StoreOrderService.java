package com.mattcom.demostoreapp.order;

import com.mattcom.demostoreapp.cart.ShoppingCartQuantities;
import com.mattcom.demostoreapp.cart.ShoppingCartQuantitiesRepository;
import com.mattcom.demostoreapp.order.exception.*;
import com.mattcom.demostoreapp.order.reqres.PaymentResponse;
import com.mattcom.demostoreapp.requestmodels.CreateOrderRequest;
import com.mattcom.demostoreapp.user.StoreUser;
import com.mattcom.demostoreapp.user.address.Address;
import com.mattcom.demostoreapp.user.address.AddressRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StoreOrderService {
    private final StoreOrderRepository storeOrderRepository;
    private final ShoppingCartQuantitiesRepository shoppingCartQuantitiesRepository;
    private final AddressRepository addressRepository;

    @Value("${stripe.api.key.sk}")
    private String stripeKey;

    public StoreOrderService(StoreOrderRepository storeOrderRepository, ShoppingCartQuantitiesRepository shoppingCartQuantitiesRepository, AddressRepository addressRepository) {
        this.storeOrderRepository = storeOrderRepository;
        this.shoppingCartQuantitiesRepository = shoppingCartQuantitiesRepository;
        this.addressRepository = addressRepository;
    }

    public List<StoreOrder> getOrders(StoreUser user) {
        return storeOrderRepository.findByUser(user);
    }

    public List<StoreOrder> getOrdersByProduct(int productId) {
        return storeOrderRepository.findByQuantities_Product_Id(productId);
    }

    public PaymentResponse createOrder(StoreUser user, CreateOrderRequest createOrderRequest)  {
        List<ShoppingCartQuantities> shoppingCartQuantities = shoppingCartQuantitiesRepository.findByShoppingCart_User_Id(user.getId());
        if(shoppingCartQuantities.isEmpty()) {
            throw new NoItemsInCartException("Cart is empty");
        }
        Optional<Address> address = addressRepository.findById(createOrderRequest.getAddressId());
        if (address.isEmpty()) {
            throw new AddressNotFoundException("Could not find address with id of " + createOrderRequest.getAddressId());
        }
        StoreOrder order = new StoreOrder();
        order.setUser(user);
        for (ShoppingCartQuantities cartQuantity : shoppingCartQuantities) {
            order.addCartQuantityToOrder(cartQuantity);
        }
        order.setTotalPrice(order.calculateTotalPrice());
        order.setAddress(address.get());
        order = storeOrderRepository.save(order);

        return createPaymentLink(order);
    }

    public void completeOrder(int orderId) {
        Optional<StoreOrder> order = storeOrderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException(orderId);
        }
        if (order.get().getIsPaid()) {
            return;
        }
        order.get().setIsPaid(true);
        storeOrderRepository.save(order.get());
    }

    public void cancelOrder(int orderId) {
        Optional<StoreOrder> order = storeOrderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException(orderId);
        }
        storeOrderRepository.delete(order.get());
    }

    private SessionCreateParams.LineItem createLineItem(StoreOrderQuantities shoppingCartQuantities) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(shoppingCartQuantities.getQuantity()))
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .putMetadata("product_id", String.valueOf(shoppingCartQuantities.getProduct().getId()))
                                                .setName(shoppingCartQuantities.getProduct().getProductName())
                                                .build()
                                )
                                .setCurrency("usd")
                                .setUnitAmountDecimal(BigDecimal.valueOf(shoppingCartQuantities.getProduct().getPrice() * 100)).build()
                ).build();
    }


    public PaymentResponse createPaymentLink(StoreOrder order)  {
        Stripe.apiKey = stripeKey;

        SessionCreateParams.Builder params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/order/success")
                .setCancelUrl("http://localhost:3000/order/failure")
                .setCustomerEmail(order.getUser().getEmail());


        for (StoreOrderQuantities storeOrderQuantities : order.getQuantities()) {
            params.addLineItem(createLineItem(storeOrderQuantities));
        }



        params.putMetadata("order_id", String.valueOf(order.getId())).putMetadata("user_id", String.valueOf(order.getUser().getId()));
        SessionCreateParams com = params.build();


        try{
            Session session = Session.create(com);
            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setPaymentUrl(session.getUrl());
            return paymentResponse;
        } catch (StripeException ex){
            throw new StripeFailureException(ex.getMessage());
        }

    }

    public StoreOrder getOrder(StoreUser user, int orderId) {
        Optional<StoreOrder> storeOrderOpt = storeOrderRepository.findById(orderId);
        if(storeOrderOpt.isEmpty()){
            throw new OrderNotFoundException(orderId);
        }
        StoreOrder storeOrder = storeOrderOpt.get();
        if(!storeOrder.getUser().getId().equals(user.getId())){
            throw new IncorrectUserToOrderException();
        }
        return storeOrder;
    }

    public List<StoreOrder> getOrdersBetween(LocalDateTime orderDateStart, LocalDateTime orderDateEnd) {
        return storeOrderRepository.findByCreatedAtBetween(orderDateStart, orderDateEnd);
    }
}
