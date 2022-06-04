package com.gratico.projects.martrustexam.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Builder
@Getter
public class ConversionResult {
    private LocalDate date;
    private String historical;
    private Map<String, Object> info;
    private Map<String, Object> query;
    private BigDecimal result;
    private boolean success;
}
