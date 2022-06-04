package com.gratico.projects.martrustexam.controller;

import com.gratico.projects.martrustexam.model.Currency;
import com.gratico.projects.martrustexam.model.ExchangeRate;
import com.gratico.projects.martrustexam.model.Input;
import com.gratico.projects.martrustexam.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService service;

    @GetMapping(value = "/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Currency> getCurrencies() {
        return service.getCurrencies();
    }

    @GetMapping(value = "/exchange-rate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ExchangeRate> getExchangeRate(@RequestParam String from, @RequestParam String to) {
        return service.getExchangeRate(from.toUpperCase(), to.toUpperCase());
    }

    @GetMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ExchangeRate> convert(@Valid Input input) {
        return service.convert(input);
    }
}
