package com.github.ko4evneg.config.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WinCombinations {
    @JsonProperty("same_symbol_3_times")
    private Combination sameSymbol3times;
    @JsonProperty("same_symbol_4_times")
    private Combination sameSymbol4times;
    @JsonProperty("same_symbol_5_times")
    private Combination sameSymbol5times;
    @JsonProperty("same_symbol_6_times")
    private Combination sameSymbol6times;
    @JsonProperty("same_symbol_7_times")
    private Combination sameSymbol7times;
    @JsonProperty("same_symbol_8_times")
    private Combination sameSymbol8times;
    @JsonProperty("same_symbol_9_times")
    private Combination sameSymbol9times;
    private Combination sameSymbolsHorizontally;
    private Combination sameSymbolsVertically;
    private Combination sameSymbolsDiagonallyLeftToRight;
    private Combination sameSymbolsDiagonallyRightToLeft;
}
