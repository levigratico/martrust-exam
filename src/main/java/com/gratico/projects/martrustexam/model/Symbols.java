package com.gratico.projects.martrustexam.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class Symbols {
    private boolean success;
    private Map<String, String> symbols;
}
