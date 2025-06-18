package com.github.ko4evneg.config.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record GameConfig(
        @JsonProperty(defaultValue = "3") int columns,
        @JsonProperty(defaultValue = "3") int rows,
        Probabilities probabilities,
        Map<String, Symbol> symbols,
        Map<String, Combination> winCombinations
) {
}
