package com.github.ko4evneg.config.game;

import lombok.Setter;

import java.util.List;

@Setter
public class Probabilities {
    private List<SymbolProbability> standardSymbols;
    private SymbolProbability bonusSymbols;
}
