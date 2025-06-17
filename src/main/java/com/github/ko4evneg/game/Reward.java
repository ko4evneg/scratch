package com.github.ko4evneg.game;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class Reward {
    private final BigDecimal amount;

    public Reward(List<SymbolWinCombination> winCombinations, List<String> fieldBonuses, BigDecimal bettingAmount) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        Map<String, List<SymbolWinCombination>> winCombinationsBySymbol = winCombinations.stream()
                .collect(Collectors.groupingBy(SymbolWinCombination::symbol));
        for (Map.Entry<String, List<SymbolWinCombination>> entry : winCombinationsBySymbol.entrySet()) {
            String symbol = entry.getKey();
            BigDecimal symbolAmount = bettingAmount;
            for (SymbolWinCombination symbolWinCombination : entry.getValue()) {
                BigDecimal rewardMultiplier = symbolWinCombination.getRewardMultiplier();
                symbolAmount = symbolAmount.multiply(rewardMultiplier);
            }
            totalAmount = totalAmount.add(symbolAmount);
        }

        amount = totalAmount;
    }
}
