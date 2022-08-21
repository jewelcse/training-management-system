package com.training.erp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn1dLCJpYXQiOjE2NTg3MTY0ODUsImV4cCI6MTY1ODgxNjQ4NX0.t8ASgUYNPIKmtxy462oysCCkTw_E0-9LoAJWq3wwzZQEn0a-c7WXU2kfaXfa4gbEgqdl14XgpwGIyFjPwSC7AQ";

    @Test
    public void getUsersTest() throws Exception {
        this.mockMvc.perform(get("/api/v1/users")
                .header(HttpHeaders.AUTHORIZATION,
                "Bearer " +token ))
                .andExpect(status().isOk());
    }



    @Test
    public void loginTest() throws Exception {
        String requestBody="{\"username\": \"admin\", \"password\": \"admin\" } ";
        this.mockMvc.perform(post("/api/v1/sign-in")
                .content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getRolesTest() throws Exception {
        this.mockMvc.perform(get("/api/v1/roles")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " +token ))
                .andExpect(status().isOk());
    }
}
