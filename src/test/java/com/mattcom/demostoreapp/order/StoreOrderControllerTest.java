package com.mattcom.demostoreapp.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mattcom.demostoreapp.order.reqres.PaymentResponse;
import com.mattcom.demostoreapp.requestmodels.CreateOrderRequest;
import com.mattcom.demostoreapp.user.exception.UserNotLoggedInException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StoreOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithUserDetails("user1@example.com")
    public void testGetOrders_ValidUser() throws Exception {
        mockMvc.perform(get("/order")).andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    List<StoreOrder> orders = new ObjectMapper().findAndRegisterModules().readValue(content, new TypeReference<List<StoreOrder>>() {
                    });
                    for (StoreOrder order : orders) {
                        Assertions.assertEquals("user1@example.com", order.getUser().getEmail(), "The customer email should be user1@example.com");
                    }
                });
    }

    @Test
    public void testGetOrders_NoAuth() throws Exception {
        mockMvc.perform(get("/order")).andExpect(status().isUnauthorized());
    }//    @WithUserDetails("user3@example.com")
//    public void testCreateOrder_Success() throws Exception {
//        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
//        createOrderRequest.setAddressId(2);
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(MockMvcRequestBuilders.post("/order")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createOrderRequest))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(result -> {
//                    String content = result.getResponse().getContentAsString();
//                    PaymentResponse paymentResponse = new ObjectMapper().findAndRegisterModules().readValue(content, PaymentResponse.class);
//                    assertNotNull(paymentResponse, "The payment response should not be null");
//                    assertTrue( paymentResponse.getPaymentUrl().contains("stripe"), "The payment url should contain stripe");
//                });
    //}

//    @Test
//    @Transactional
//    @WithUserDetails("user3@example.com")
//    public void testCompleteOrder_Success() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/order/complete/4"))
//                .andExpect(status().isOk());
//    }

    @Test
    @WithUserDetails("user1@example.com")
    public void testGetOrder_ValidUser_ValidOrderNumber() throws Exception {
        mockMvc.perform(get("/order/1")).andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    StoreOrder order = new ObjectMapper().findAndRegisterModules().readValue(content, new TypeReference<StoreOrder>() {
                    });
                    assertNotNull(order, "The order should not be null");
                    assertEquals("user1@example.com", order.getUser().getEmail(), "The customer email should be user1@example.com");
                });
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void testGetOrder_ValidUser_InValidOrderNumber() throws Exception {
        mockMvc.perform(get("/order/700")).andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void testGetOrder_ValidUser_OrderForAnotherUser() throws Exception {
        mockMvc.perform(get("/order/3")).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void testGetOrdersBetween_ValidUser_ValidDates() throws Exception {
        long dateStart = 1649851320000L; //2022/04/13 14:02
        long dateEnd = 1651838520000L; //2022/05/06 14:02

        mockMvc.perform(get("/order/orders-between?orderDateStart=" + dateStart + "&orderDateEnd=" + dateEnd)).andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    List<StoreOrder> orders = new ObjectMapper().findAndRegisterModules().readValue(content, new TypeReference<List<StoreOrder>>() {
                    });
                    assertNotNull(orders, "The orders should not be null");
                    assertEquals(1, orders.size(), "There should be 1 order");
                    for (StoreOrder order : orders) {
                        Assertions.assertEquals("user1@example.com", order.getUser().getEmail(), "The customer email should be user1@example.com");
                    }
                });
    }



    @Test
    @WithUserDetails("user3@example.com")
    public void testGetOrdersBetween_NonAdmin() throws Exception {
        long dateStart = 1649851320000L; //2022/04/13 14:02
        long dateEnd = 1651838520000L; //2022/05/06 14:02
        mockMvc.perform(get("/order/orders-between?orderDateStart=" + dateStart + "&orderDateEnd=" + dateEnd)).andExpect(status().isForbidden());
    }

    @Test
    public void testGetOrdersByProduct() throws Exception {
        mockMvc.perform(get("/order/products/1")).andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    List<StoreOrder> orders = new ObjectMapper().findAndRegisterModules().readValue(content, new TypeReference<List<StoreOrder>>() {
                    });
                    assertNotNull(orders, "The orders should not be null");
                    assertEquals(1, orders.size(), "There should be 1 order");
                });
    }

    @Test
    @WithUserDetails("user1@example.com")
    @Transactional
    public void testCreateOrder_NoItemsInCart() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setAddressId(1);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @Transactional
    public void testCreateOrder_NotLoggedIn() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setAddressId(1);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

//    @Transactional
//    @Test




}