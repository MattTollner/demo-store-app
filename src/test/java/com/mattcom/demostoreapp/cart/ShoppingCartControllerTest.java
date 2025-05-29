package com.mattcom.demostoreapp.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mattcom.demostoreapp.cart.reqres.ShoppingCartQuantityRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetCart_NoAuth() throws Exception {
        mockMvc.perform(get("/cart")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user3@example.com")
    public void testGetCart_WithAuth_ExistCart() throws Exception {
        mockMvc.perform(get("/cart")).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user1@example.com")
    public void testGetCart_WithAuth_NoExistCart() throws Exception {
        mockMvc.perform(get("/cart")).andExpect(status().isOk());
    }


    @Test
    @WithUserDetails("user3@example.com")
    public void testUpdateCart_WithAuth() throws Exception {
        String requestString = createShoppingCartQuantityRequest();
        mockMvc.perform(put("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateCart_NoAuth() throws Exception {
        String requestString = createShoppingCartQuantityRequest();
        mockMvc.perform(put("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isUnauthorized());
    }

    private String createShoppingCartQuantityRequest() throws JsonProcessingException {
        ShoppingCartQuantityRequest request = new ShoppingCartQuantityRequest();
        request.setId(1);
        request.setQuantity(1);
        request.setProductId(1);
        return new ObjectMapper().findAndRegisterModules().writeValueAsString(request);
    }

}