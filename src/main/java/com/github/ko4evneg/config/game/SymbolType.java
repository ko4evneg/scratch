package com.github.ko4evneg.config.game;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SymbolType {
    @JsonProperty("standard")
    STANDARD,
    @JsonProperty("bonus")
    BONUS
}
