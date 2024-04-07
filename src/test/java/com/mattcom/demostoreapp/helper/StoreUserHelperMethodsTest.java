package com.mattcom.demostoreapp.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mattcom.demostoreapp.requestmodels.StoreUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StoreUserHelperMethodsTest {

    public void testUserValidation(MockMvc mockMvc, String endpoint, boolean isPost) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        //Validation for email
        mockMvc.perform(isPost ? post(endpoint) : put(endpoint)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(fakeInfo("invalidEmail", "test", "test", "test"))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        mockMvc.perform(isPost ? post(endpoint) : put(endpoint)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(fakeInfo("", "test", "test", "test"))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        mockMvc.perform(isPost ? post(endpoint) : put(endpoint)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(fakeInfo(null, "test", "test", "test"))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        //Validation for first name
        mockMvc.perform(isPost ? post(endpoint) : put(endpoint)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(fakeInfo("testRegisterUser@example.com", "", "test", "test"))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        mockMvc.perform(isPost ? post(endpoint) : put(endpoint)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(fakeInfo("testRegisterUser@example.com", null, "test", "test"))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        //Validation for last name
        mockMvc.perform(isPost ? post(endpoint) : put(endpoint)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(fakeInfo("testRegisterUser@example.com", "test", "", "test"))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        mockMvc.perform(isPost ? post(endpoint) : put(endpoint)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(fakeInfo("testRegisterUser@example.com", "test", null, "test"))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        //Validation for last password
        mockMvc.perform(isPost ? post(endpoint) : put(endpoint)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(fakeInfo("testRegisterUser@example.com", "test", "test", ""))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        mockMvc.perform(isPost ? post(endpoint) : put(endpoint)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(fakeInfo("testRegisterUser@example.com", "test", "test", null))))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        //Test valid user
        mockMvc.perform(isPost ? post(endpoint) : put(endpoint)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(fakeInfo("testRegisterUser@example.com", "test", "test", "test"))))
                .andExpect(status().is(HttpStatus.OK.value()));


    }

    private StoreUserRequest fakeInfo(String email, String firstName, String lastName, String password) {
        StoreUserRequest registrationInfo = new StoreUserRequest();
        registrationInfo.setEmail(email);
        registrationInfo.setFirstName(firstName);
        registrationInfo.setLastName(lastName);
        registrationInfo.setPassword(password);
        return registrationInfo;
    }
}
