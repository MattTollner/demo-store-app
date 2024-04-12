package com.mattcom.demostoreapp.webhook;

import com.google.gson.JsonSyntaxException;
import com.mattcom.demostoreapp.cart.ShoppingCartService;
import com.mattcom.demostoreapp.order.StoreOrderService;
import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@Controller
public class StripeWebhook {

    @Value("${stripe.api.key.sk}")
    private String apiKey;

    private final StoreOrderService storeOrderService;

    private final ShoppingCartService shoppingCartService;

    // Stripe.apiKey = "sk_test_...";

    // This is your Stripe CLI webhook secret for testing your endpoint locally.
    String endpointSecret = "whsec_013ad49ad23ea1ab027169c044f28d988cad8c2493810bd69fd8a4bc19605dbb";

    public StripeWebhook(StoreOrderService storeOrderService, ShoppingCartService shoppingCartService) {
        this.storeOrderService = storeOrderService;
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handlePostPaymentEvent(@RequestHeader("Stripe-Signature") String sigHeader,
                                                         @RequestBody String payload) throws Exception {

        Stripe.apiKey = apiKey;

        Event event = null;


        try {
            event = ApiResource.GSON.fromJson(payload, Event.class);

            System.out.println(event.getType());
            if (event.getType().equals("checkout.session.completed")) {
                System.out.println("correct");
                int orderId = getMetaData(event, "order_id");
                int userId = getMetaData(event, "user_id");
                storeOrderService.completeOrder(orderId);
                shoppingCartService.clearCart(userId);
            }
            System.out.println("here");
        } catch (JsonSyntaxException e) {
            // Invalid payload
            // response.status(400);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }


    private int getMetaData(Event event, String key) throws Exception {
        if (event != null && event.getDataObjectDeserializer() != null) {
            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            if (deserializer.getObject().isPresent() && deserializer.getObject().get() instanceof Session) {

                String metadataValue = ((Session) deserializer.getObject().get()).getMetadata().get(key);
                return Integer.parseInt(metadataValue);
            }
        }
        throw new Exception("Meta data not found");
    }
}
