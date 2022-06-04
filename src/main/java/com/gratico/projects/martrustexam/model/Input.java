package com.gratico.projects.martrustexam.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Builder
public class Input {
    @NotNull
    private String from;
    @NotNull
    private String to;
    @NotNull
    private BigDecimal amount;
}
