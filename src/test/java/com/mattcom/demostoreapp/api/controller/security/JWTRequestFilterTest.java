package com.mattcom.demostoreapp.api.controller.security;

import com.mattcom.demostoreapp.auth.JWTService;
import com.mattcom.demostoreapp.user.StoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class JWTRequestFilterTest {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private StoreUserRepository storeUserRepository;

    @Autowired
    MockMvc mockMvc;

    private static final String AUTHENTICATED_PATH ="/auth/me";

//    @Test
//    public void testUnauthenticatedRequest() throws Exception {
//        mockMvc.perform(get(AUTHENTICATED_PATH)).andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void testInvalidToken() throws Exception {
//        mockMvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Invalid Token ")).andExpect(status().isForbidden());
//        mockMvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Bearer invalid")).andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void testUnverifiedUser() throws Exception {
//        String token = jwtService.gererateJWT(storeUserRepository.findByEmailIgnoreCase("user2@example.com").get());
//        mockMvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Bearer " + token)).andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void testValidUser() throws Exception {
//        String token = jwtService.gererateJWT(storeUserRepository.findByEmailIgnoreCase("user1@example.com").get());
//        mockMvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Bearer " + token)).andExpect(status().isOk());
//    }
}
