package com.github.ko4evneg.config.game;

import java.util.Map;

// TODO:  add defaults
public record GameConfig(int columns, int rows, Probabilities probabilities, Map<String, Symbol> symbols,
                         Map<String, Combination> winCombinations) {
}
