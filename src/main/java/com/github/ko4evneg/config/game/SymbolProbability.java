package com.github.ko4evneg.config.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SymbolProbability {
    private int row;
    private int column;
    private Map<String, Integer> symbols;
}
