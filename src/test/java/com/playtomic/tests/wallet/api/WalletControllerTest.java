package com.playtomic.tests.wallet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.domain.dto.ChargeDto;
import com.playtomic.tests.wallet.domain.dto.WalletDto;
import com.playtomic.tests.wallet.domain.dto.WithdrawDto;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.playtomic.tests.wallet.utils.TestUtils.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private WalletService walletService;
    @Autowired
    private MockMvc mvc;

    @Test
    void getByID() throws Exception {
        WalletDto walletDto = getWalletDto();
        final String id = walletDto.getWalletId();
        when(walletService.getWalletById(id)).thenReturn(walletDto);
        mvc.perform(get("/wallets/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(id));
    }

    @Test
    void charge() throws Exception {
        ChargeDto chargeDto = getChargeDto();
        String json = objectMapper.writeValueAsString(chargeDto);
        doNothing().when(walletService).charge(chargeDto);
        mvc.perform(post("/wallets/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void withdraw() throws Exception {
        WithdrawDto withdrawDto = getWithdrawDto();
        String json = objectMapper.writeValueAsString(withdrawDto);
        doNothing().when(walletService).withdraw(withdrawDto);
        mvc.perform(post("/wallets/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }
}