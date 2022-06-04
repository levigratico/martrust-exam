package com.gratico.projects.martrustexam.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class CurrencyRate {
    private String currency;
    private BigDecimal rate;
}
