package com.github.ko4evneg.config.game;

import java.util.List;

public record Probabilities(List<SymbolProbability> standardSymbols, SymbolProbability bonusSymbols) {
}
