package com.gratico.projects.martrustexam.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gratico.projects.martrustexam.model.Currency;
import com.gratico.projects.martrustexam.model.CurrencyRate;
import com.gratico.projects.martrustexam.model.ExchangeRate;
import com.gratico.projects.martrustexam.service.ExchangeRateService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateControllerTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ExchangeRateController exchangeRateController;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(exchangeRateController)
                .build();
    }

    @SneakyThrows
    @Test
    public void testGetCurrencies() {
        when(exchangeRateService.getCurrencies()).thenReturn(createCurrencyTestData());
        MvcResult result = mockMvc.perform(get("/currencies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();
        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencies").isArray())
                .andExpect(jsonPath("$.currencies", containsInAnyOrder("PHP", "USD")));
    }

    @SneakyThrows
    @Test
    public void testGetExchangeRate() {
        when(exchangeRateService.getExchangeRate(any(), any())).thenReturn(createExchangeRateTestData());
        MvcResult result = mockMvc.perform(get("/exchange-rate")
                .queryParam("from", "USD")
                .queryParam("to", "PHP")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();
        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.to.rate", is(52.91)))
                .andExpect(jsonPath("$.to.currency", is("PHP")))
                .andExpect(jsonPath("$.from.rate", is(1)))
                .andExpect(jsonPath("$.from.currency", is("USD")));
    }

    @SneakyThrows
    @Test
    public void testConvert() {
        when(exchangeRateService.convert(any())).thenReturn(createExchangeRateTestData());
        MvcResult result = mockMvc.perform(get("/convert")
                        .queryParam("from", "USD")
                        .queryParam("to", "PHP")
                        .queryParam("amount", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();
        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.to.rate", is(52.91)))
                .andExpect(jsonPath("$.to.currency", is("PHP")))
                .andExpect(jsonPath("$.from.rate", is(1)))
                .andExpect(jsonPath("$.from.currency", is("USD")));
    }

    private Mono<ExchangeRate> createExchangeRateTestData() {
        return Mono.just(ExchangeRate.builder()
                .to(CurrencyRate.builder()
                        .rate(BigDecimal.valueOf(52.91))
                        .currency("PHP")
                        .build())
                .from(CurrencyRate.builder()
                        .rate(BigDecimal.ONE)
                        .currency("USD")
                        .build())
                .build());
    }

    private Mono<Currency> createCurrencyTestData() {
        return Mono.just(Currency.builder()
                .currencies(Set.of("PHP", "USD"))
                .build());
    }
}
