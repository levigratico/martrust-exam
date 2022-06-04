package com.gratico.projects.martrustexam.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class Currency {
    private Set<String> currencies;
}
