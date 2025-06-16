package com.github.ko4evneg.config.game;

import lombok.Setter;

import java.util.Map;

@Setter
public class SymbolProbability {
    private int row;
    private int column;
    private Map<String, Integer> symbols;
}
