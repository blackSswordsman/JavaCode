package com.example.javacode.controller;

import com.example.javacode.controller.dto.ChangeWalletBalanceRequest;
import com.example.javacode.dao.OperationType;
import com.example.javacode.dao.Wallet;
import com.example.javacode.dto.ChangeWalletBalanceRecord;
import com.example.javacode.exception.InsufficientFundsException;
import com.example.javacode.exception.WalletNotFoundException;
import com.example.javacode.service.api.WalletService;
import com.example.javacode.service.impl.WalletServiceImpl;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
class WalletControllerTest {

    @MockBean
    WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostRequestToWalletAndValidChangeBalanceWalletRequest_thenCorrectResponse() throws Exception {
        // given
        UUID uuid = UUID.fromString("5a656e0f-6b7c-46e5-8bc7-24745dec1b86");
        ChangeWalletBalanceRecord changeWalletBalanceRecord = new ChangeWalletBalanceRecord
                (uuid, OperationType.WITHDRAW, BigDecimal.valueOf(800000000));
        when(walletService.changeBalance(changeWalletBalanceRecord)).thenReturn(new Wallet()
                .setId(uuid)
                .setBalance(BigDecimal.valueOf(5952)));
        String changeWalletBalanceRequest = "{\"walletId\":\"5a656e0f-6b7c-46e5-8bc7-24745dec1b86\"," +
                "\"operationType\": \"WITHDRAW\"," +
                "\"sum\": 800000000}";
        String expectedResponse = "{\"id\":\"5a656e0f-6b7c-46e5-8bc7-24745dec1b86\",\"balance\":5952}";

        // when then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
                        .content(changeWalletBalanceRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void whenPostRequestToWalletAndValidChangeBalanceWalletRequestButWalletServiceThrowsInsufficientFundsException_thenResponseWithError() throws Exception {
        // given
        UUID uuid = UUID.fromString("5a656e0f-6b7c-46e5-8bc7-24745dec1b86");
        ChangeWalletBalanceRecord changeWalletBalanceRecord = new ChangeWalletBalanceRecord
                (uuid, OperationType.WITHDRAW, BigDecimal.valueOf(800000000));
        when(walletService.changeBalance(changeWalletBalanceRecord)).thenThrow(new InsufficientFundsException());
        String changeWalletBalanceRequest = "{\"walletId\":\"5a656e0f-6b7c-46e5-8bc7-24745dec1b86\"," +
                "\"operationType\": \"WITHDRAW\"," +
                "\"sum\": 800000000}";
        String expectedResponse = "{\"message\":\"Insufficient Funds\"}";

        // when then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
                        .content(changeWalletBalanceRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void whenPostRequestToWalletAndChangeBalanceWalletRequestWithEmptyWalletId_thenResponseWithError() throws Exception {
        // given
        String changeWalletBalanceRequest = "{\"walletId\":null," +
                "\"operationType\": \"WITHDRAW\"," +
                "\"sum\": 800000000}";
        String expectedResponse = "{\"walletId\":\"wallet id must be not null\"}";

        // when then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
                        .content(changeWalletBalanceRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void whenPostRequestToWalletAndChangeBalanceWalletRequestWithEmptyOperationType_thenResponseWithError() throws Exception {
        // given
        String changeWalletBalanceRequest = "{\"walletId\":\"5a656e0f-6b7c-46e5-8bc7-24745dec1b86\"," +
                "\"operationType\": null," +
                "\"sum\": 800000000}";
        String expectedResponse = "{\"operationType\":\"operation type must be not null\"}";

        // when then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
                        .content(changeWalletBalanceRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void whenPostRequestToWalletAndChangeBalanceWalletRequestWithEmptySum_thenResponseWithError() throws Exception {
        // given
        String changeWalletBalanceRequest = "{\"walletId\":\"5a656e0f-6b7c-46e5-8bc7-24745dec1b86\"," +
                "\"operationType\": \"WITHDRAW\"," +
                "\"sum\": null}";
        String expectedResponse = "{\"sum\":\"sum must be not null\"}";

        // when then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
                        .content(changeWalletBalanceRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void whenPostRequestToWalletAndChangeBalanceWalletRequestWithNegativeSum_thenResponseWithError() throws Exception {
        // given
        String changeWalletBalanceRequest = "{\"walletId\":\"5a656e0f-6b7c-46e5-8bc7-24745dec1b86\"," +
                "\"operationType\": \"WITHDRAW\"," +
                "\"sum\": -900}";
        String expectedResponse = "{\"sum\":\"sum must be positive\"}";

        // when then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
                        .content(changeWalletBalanceRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void whenPostRequestToCrateNewWallet_thenCorrectResponse() throws Exception {
        // given
        when(walletService.createNewWallet()).thenReturn(new Wallet()
                .setId(UUID.fromString("5a656e0f-6b7c-46e5-8bc7-24745dec1b88"))
                .setBalance(BigDecimal.valueOf(0)));
        String expectedResponse = "{\"id\":\"5a656e0f-6b7c-46e5-8bc7-24745dec1b88\",\"balance\":0}";

        // when then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/create_wallet")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void whenGetRequestToWalletAndValidId_thenCorrectResponse() throws Exception {
        // given
        UUID uuid = UUID.fromString("5a656e0f-6b7c-46e5-8bc7-24745dec1b88");
        when(walletService.getWallet(uuid)).thenReturn(new Wallet()
                .setId(uuid)
                .setBalance(BigDecimal.valueOf(0)));
        String expectedResponse = "{\"id\":\"5a656e0f-6b7c-46e5-8bc7-24745dec1b88\",\"balance\":0}";

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wallets/{id}", "5a656e0f-6b7c-46e5-8bc7-24745dec1b88")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void whenGetRequestToWalletAndValidIdButWalletServiceThrowsWalletNotFoundException_thenResponseWithError() throws Exception {
        // given
        UUID uuid = UUID.fromString("5a656e0f-6b7c-46e5-8bc7-24745dec1b88");
        when(walletService.getWallet(uuid)).thenThrow(new WalletNotFoundException());
        String expectedResponse = "{\"message\":\"Wallet not found\"}";

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wallets/{id}", "5a656e0f-6b7c-46e5-8bc7-24745dec1b88")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResponse));
    }



}