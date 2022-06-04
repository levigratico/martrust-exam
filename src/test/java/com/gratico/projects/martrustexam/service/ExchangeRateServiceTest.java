package com.gratico.projects.martrustexam.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gratico.projects.martrustexam.model.ConversionResult;
import com.gratico.projects.martrustexam.model.Currency;
import com.gratico.projects.martrustexam.model.ExchangeRate;
import com.gratico.projects.martrustexam.model.Input;
import com.gratico.projects.martrustexam.model.Rates;
import com.gratico.projects.martrustexam.model.Symbols;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeaders;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUri;

    @Mock
    private WebClient.ResponseSpec response;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Test
    public void testGetCurrencies() {
        when(webClient.get()).thenReturn(requestHeaders);
        when(requestHeaders.uri("/symbols")).thenReturn(requestHeadersUri);
        when(requestHeadersUri.retrieve()).thenReturn(response);
        when(response.bodyToMono(Symbols.class)).thenReturn(createSymbolsTestData());
        Mono<Currency> actual = exchangeRateService.getCurrencies();

        StepVerifier.create(actual)
                .expectNextMatches(currency -> currency.getCurrencies().containsAll(Set.of("USD", "PHP")))
                .verifyComplete();
    }

    @Test
    public void testGetExchangeRate() {
        when(webClient.get()).thenReturn(requestHeaders);
        when(requestHeaders.uri(any(), any(Function.class))).thenReturn(requestHeaders);
        when(requestHeaders.retrieve()).thenReturn(response);
        when(response.bodyToMono(Rates.class)).thenReturn(createRatesTestData());
        Mono<ExchangeRate> actual = exchangeRateService.getExchangeRate("USD", "PHP");
        StepVerifier.create(actual)
                .expectNextMatches(exchangeRate -> exchangeRate.getFrom().getRate().equals(BigDecimal.ONE)
                        && exchangeRate.getFrom().getCurrency().equals("USD")
                        && exchangeRate.getTo().getRate().equals(BigDecimal.valueOf(52.92))
                        && exchangeRate.getTo().getCurrency().equals("PHP"))
                .verifyComplete();
    }

    @Test
    public void testConvert() {
        when(webClient.get()).thenReturn(requestHeaders);
        when(requestHeaders.uri(any(), any(Function.class))).thenReturn(requestHeaders);
        when(requestHeaders.retrieve()).thenReturn(response);
        when(response.bodyToMono(ConversionResult.class)).thenReturn(createConversionResultTestData());
        Input input = Input.builder()
                .from("USD")
                .to("PHP")
                .amount(BigDecimal.ONE)
                .build();
        Mono<ExchangeRate> actual = exchangeRateService.convert(input);
        StepVerifier.create(actual)
                .expectNextMatches(exchangeRate -> exchangeRate.getFrom().getRate().equals(BigDecimal.ONE)
                        && exchangeRate.getFrom().getCurrency().equals("USD")
                        && exchangeRate.getTo().getRate().equals(BigDecimal.valueOf(52.92))
                        && exchangeRate.getTo().getCurrency().equals("PHP"))
                .verifyComplete();
    }

    private Mono<Symbols> createSymbolsTestData() {
        return Mono.just(Symbols.builder()
                .symbols(Map.of("USD", "United State Dollars", "PHP", "Philippine Peso"))
                .success(true)
                .build());
    }

    private Mono<Rates> createRatesTestData() {
        return Mono.just(Rates.builder()
                .base("USD")
                .date(LocalDate.now())
                .rates(Collections.singletonMap("PHP", BigDecimal.valueOf(52.92)))
                .success(true)
                .timestamp(LocalDate.now().toEpochDay())
                .build());
    }

    private Mono<ConversionResult> createConversionResultTestData() {
        return Mono.just(ConversionResult.builder()
                .date(LocalDate.now())
                .result(BigDecimal.valueOf(52.92))
                .historical("")
                .info(Collections.singletonMap("test", "test"))
                .success(true)
                .query(Collections.singletonMap("test", "test"))
                .build());
    }
}
