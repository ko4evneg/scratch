package com.github.ko4evneg.game;

import com.github.ko4evneg.config.game.Combination;

import java.math.BigDecimal;

public record SymbolWinCombination(String symbol, String combinationName, Combination combination) {
    public BigDecimal getRewardMultiplier() {
        return combination.rewardMultiplier();
    }
}
