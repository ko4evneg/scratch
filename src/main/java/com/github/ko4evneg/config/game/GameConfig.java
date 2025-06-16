package com.github.ko4evneg.config.game;

import lombok.Setter;

import java.util.Map;

@Setter
public class GameConfig {
    private int columns;
    private int rows;
    private Map<String, Symbol> symbols;
    private Probabilities probabilities;
    private WinCombinations winCombinations;
}
