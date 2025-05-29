package com.mattcom.demostoreapp.api.controller.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mattcom.demostoreapp.order.StoreOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    public void testGetOrdersNoAuth() throws Exception {
//        mockMvc.perform(get("/orders")).andExpect(status().isForbidden());
//    }

    @BeforeEach


    @Test
    @WithUserDetails("user1@example.com")
    public void testGetOrders_ValidUser() throws Exception {
        SecurityContext sc = SecurityContextHolder.getContext();
        mockMvc.perform(get("/order")).andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    List<StoreOrder> orders = new ObjectMapper().readValue(content, new TypeReference<List<StoreOrder>>() {
                    });
                    for (StoreOrder order : orders) {
                        Assertions.assertEquals("user1@example.com", order.getUser().getEmail(), "The customer email should be user1@example.com");
                    }
                });
    }
}
