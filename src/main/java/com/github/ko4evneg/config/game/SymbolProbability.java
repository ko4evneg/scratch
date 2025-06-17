package com.github.ko4evneg.config.game;

import java.util.Map;

public record SymbolProbability(
        Integer row,
        Integer column,
        Map<String, Integer> symbols
) {
}
