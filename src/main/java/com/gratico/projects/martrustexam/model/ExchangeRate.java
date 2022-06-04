package com.gratico.projects.martrustexam.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExchangeRate {
    private CurrencyRate from;
    private CurrencyRate to;
}
