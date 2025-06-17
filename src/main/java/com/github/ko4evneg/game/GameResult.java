package com.github.ko4evneg.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class GameResult {
    @JsonProperty(value = "matrix")
    private final String[][] field;

    @JsonProperty(value = "reward")
    private final BigDecimal reward;

    @JsonProperty(value = "applied_winning_combinations")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Map<String, List<String>> appliedWinningCombinations;

    @JsonProperty(value = "applied_bonus_symbol")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final String appliedBonusSymbol;

    public GameResult(Matrix matrix, BigDecimal reward, String appliedBonusSymbol, List<SymbolWinCombination> winCombinations) {
        this.field = matrix.getField();
        this.reward = reward;
        this.appliedBonusSymbol = appliedBonusSymbol;
        this.appliedWinningCombinations = winCombinations.stream()
                .collect(Collectors.groupingBy(SymbolWinCombination::symbol,
                        Collectors.mapping(SymbolWinCombination::combinationName, Collectors.toList())));
    }
}
