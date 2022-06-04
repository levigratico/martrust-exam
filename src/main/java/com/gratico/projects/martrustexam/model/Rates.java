package com.gratico.projects.martrustexam.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder
@EqualsAndHashCode
public class Rates {
    private String base;
    private LocalDate date;
    private Map<String, BigDecimal> rates;
    private boolean success;
    private long timestamp;
}
