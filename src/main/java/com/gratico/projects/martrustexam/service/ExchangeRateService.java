package com.gratico.projects.martrustexam.service;

import com.gratico.projects.martrustexam.model.ConversionResult;
import com.gratico.projects.martrustexam.model.Currency;
import com.gratico.projects.martrustexam.model.CurrencyRate;
import com.gratico.projects.martrustexam.model.ExchangeRate;
import com.gratico.projects.martrustexam.model.Input;
import com.gratico.projects.martrustexam.model.Rates;
import com.gratico.projects.martrustexam.model.Symbols;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String AMOUNT = "amount";
    private static final String BASE = "base";
    private static final String SYMBOLS = "symbols";

    private static final String PATH_SYMBOLS = "/symbols";
    private static final String PATH_LATEST = "/latest";
    private static final String PATH_CONVERT = "/convert";

    private final WebClient webClient;


    public Mono<Currency> getCurrencies() {
        return webClient.get()
                .uri(PATH_SYMBOLS)
                .retrieve()
                .bodyToMono(Symbols.class)
                .map(Symbols::getSymbols)
                .map(Map::keySet)
                .map(keys -> Currency.builder().currencies(keys).build());
    }

    public Mono<ExchangeRate> getExchangeRate(String from, String to) {
        return webClient.get()
                .uri(PATH_LATEST, uriBuilder -> uriBuilder
                        .queryParam(BASE, from)
                        .queryParam(SYMBOLS, to)
                        .build())
                .retrieve()
                .bodyToMono(Rates.class)
                .map(Rates::getRates)
                .map(rates -> rates.entrySet().stream().findFirst().orElse(null))
                .map(rate -> toExchangeRate(from, to, rate));
    }

    public Mono<ExchangeRate> convert(Input input) {
        return webClient.get()
                .uri(PATH_CONVERT, uriBuilder -> uriBuilder
                        .queryParam(FROM, input.getFrom())
                        .queryParam(TO, input.getTo())
                        .queryParam(AMOUNT, input.getAmount())
                        .build())
                .retrieve()
                .bodyToMono(ConversionResult.class)
                .map(result -> ExchangeRate.builder()
                        .from(CurrencyRate.builder()
                                .currency(input.getFrom())
                                .rate(input.getAmount())
                                .build())
                        .to(CurrencyRate.builder()
                                .currency(input.getTo())
                                .rate(result.getResult())
                                .build())
                        .build());
    }

    private ExchangeRate toExchangeRate(String from, String to, Map.Entry<String, BigDecimal> rate) {
        Map.Entry<String, BigDecimal> tempRate = Optional.ofNullable(rate).orElse(Map.entry(to, BigDecimal.ZERO));
        return ExchangeRate.builder()
                .from(CurrencyRate.builder()
                        .rate(BigDecimal.ONE)
                        .currency(from)
                        .build())
                .to(CurrencyRate.builder()
                        .rate(tempRate.getValue())
                        .currency(tempRate.getKey())
                        .build())
                .build();
    }
}
