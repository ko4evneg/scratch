package com.github.ko4evneg.config.game;

import java.util.Map;

public record SymbolProbability(
        int row,
        int column,
        Map<String, Integer> symbols
) {
}
