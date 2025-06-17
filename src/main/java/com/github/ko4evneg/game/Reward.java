package com.github.ko4evneg.game;

import com.github.ko4evneg.config.game.Symbol;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class Reward {
    private final BigDecimal amount;

    public Reward(List<SymbolWinCombination> winCombinations, List<String> fieldBonuses, Map<String, Symbol> symbols, BigDecimal bettingAmount) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        Map<String, List<SymbolWinCombination>> winCombinationsBySymbol = winCombinations.stream()
                .collect(Collectors.groupingBy(SymbolWinCombination::symbol));
        for (Map.Entry<String, List<SymbolWinCombination>> entry : winCombinationsBySymbol.entrySet()) {
            String symbol = entry.getKey();
            BigDecimal symbolAmount = bettingAmount.multiply(symbols.get(symbol).rewardMultiplier());
            for (SymbolWinCombination symbolWinCombination : entry.getValue()) {
                BigDecimal rewardMultiplier = symbolWinCombination.getRewardMultiplier();
                symbolAmount = symbolAmount.multiply(rewardMultiplier);
            }
            totalAmount = totalAmount.add(symbolAmount);
        }

        BigDecimal extraAmount = BigDecimal.ZERO;
        for (String fieldBonus : fieldBonuses) {
            Symbol bonusSymbol = symbols.get(fieldBonus);
            switch (bonusSymbol.impact()) {
                case EXTRA_BONUS -> extraAmount = extraAmount.add(bonusSymbol.extra());
                case MULTIPLY_REWARD -> totalAmount = totalAmount.multiply(bonusSymbol.rewardMultiplier());
            }
        }

        amount = totalAmount.add(extraAmount);
    }
}
