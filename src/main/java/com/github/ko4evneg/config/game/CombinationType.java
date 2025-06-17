package com.github.ko4evneg.config.game;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CombinationType {
    @JsonProperty("same_symbols")
    SAME_SYMBOLS,
    @JsonProperty("linear_symbols")
    LINEAR_SYMBOLS
}
