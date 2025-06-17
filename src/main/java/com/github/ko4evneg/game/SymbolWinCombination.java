package com.github.ko4evneg.game;

import java.math.BigDecimal;

public record SymbolWinCombination(String symbol, WinCombination combination) {
    BigDecimal getRewardMultiplier() {
        return combination.combination().rewardMultiplier();
    }
}
