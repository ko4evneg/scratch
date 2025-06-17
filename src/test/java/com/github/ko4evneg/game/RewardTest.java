package com.github.ko4evneg.game;

import com.github.ko4evneg.config.game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RewardTest {
    private final BigDecimal BETTING_AMOUNT = BigDecimal.valueOf(100);
    private Map<String, Symbol> symbols;

    @BeforeEach
    void setUp() {
        symbols = Map.of(
                "A", new Symbol(SymbolType.STANDARD, BigDecimal.valueOf(4), null, null),
                "B", new Symbol(SymbolType.STANDARD, BigDecimal.valueOf(3), null, null),
                "C", new Symbol(SymbolType.STANDARD, BigDecimal.valueOf(2.5), null, null),
                "10x", new Symbol(SymbolType.BONUS, BigDecimal.valueOf(10), null, BonusImpact.MULTIPLY_REWARD),
                "5x", new Symbol(SymbolType.BONUS, BigDecimal.valueOf(5), null, BonusImpact.MULTIPLY_REWARD),
                "+1000", new Symbol(SymbolType.BONUS, null, BigDecimal.valueOf(1000), BonusImpact.EXTRA_BONUS),
                "+500", new Symbol(SymbolType.BONUS, null, BigDecimal.valueOf(500), BonusImpact.EXTRA_BONUS)
        );
    }

    @Test
    void shouldCalculateZeroRewardWhenNoWinCombinations() {
        Reward reward = new Reward(List.of(), List.of(), symbols, BETTING_AMOUNT);

        assertThat(reward.getAmount()).isEqualTo(BigDecimal.ZERO);
    }

    @ParameterizedTest
    @CsvSource({"1,300", "2,600", "3,900"})
    void shouldCalculateBasicRewardForSingleSymbol(int combinationMultiplier, int expectedAmount) {
        Combination combination = new Combination(BigDecimal.valueOf(combinationMultiplier), CombinationType.SAME_SYMBOLS, 3, CombinationGroup.SAME_SYMBOLS, null);
        SymbolWinCombination winCombination = new SymbolWinCombination("B", "same_symbol_3_times", combination);

        Reward reward = new Reward(List.of(winCombination), List.of(), symbols, BETTING_AMOUNT);

        assertThat(reward.getAmount()).isEqualTo(BigDecimal.valueOf(expectedAmount));
    }

    @Test
    void shouldMultiplyCombinationsForSameSymbol() {
        Combination combination1 = new Combination(BigDecimal.valueOf(2), CombinationType.SAME_SYMBOLS, 3, CombinationGroup.SAME_SYMBOLS, null);
        Combination combination2 = new Combination(BigDecimal.valueOf(3), CombinationType.LINEAR_SYMBOLS, 5, CombinationGroup.VERTICALLY_LINEAR_SYMBOLS, null);
        List<SymbolWinCombination> winCombinations = List.of(
                new SymbolWinCombination("A", "same_symbol_3_times", combination1),
                new SymbolWinCombination("A", "vertically_linear_symbols", combination2)
        );

        Reward reward = new Reward(winCombinations, List.of(), symbols, BETTING_AMOUNT);

        assertThat(reward.getAmount()).isEqualTo(BigDecimal.valueOf(2400));
    }


    @Test
    void shouldSumRewardsForDifferentSymbols() {
        Combination combination1 = new Combination(BigDecimal.valueOf(2), CombinationType.SAME_SYMBOLS, 3, CombinationGroup.SAME_SYMBOLS, null);
        Combination combination2 = new Combination(BigDecimal.valueOf(3), CombinationType.LINEAR_SYMBOLS, 5, CombinationGroup.VERTICALLY_LINEAR_SYMBOLS, null);

        List<SymbolWinCombination> winCombinations = List.of(
                new SymbolWinCombination("A", "same_symbol_3_times", combination1),
                new SymbolWinCombination("B", "vertically_linear_symbols", combination2)
        );

        Reward reward = new Reward(winCombinations, List.of(), symbols, BETTING_AMOUNT);

        assertThat(reward.getAmount()).isEqualTo(BigDecimal.valueOf(1700));
    }

    @Test
    void shouldApplyMultiplyBonusToTotalReward() {
        Combination combination = new Combination(BigDecimal.valueOf(1), CombinationType.SAME_SYMBOLS, 3, CombinationGroup.SAME_SYMBOLS, null);
        SymbolWinCombination winCombination = new SymbolWinCombination("B", "same_symbol_3_times", combination);

        Reward reward = new Reward(List.of(winCombination), List.of("10x"), symbols, BETTING_AMOUNT);

        assertThat(reward.getAmount()).isEqualTo(BigDecimal.valueOf(3000));
    }

    @Test
    void shouldApplyExtraBonusToFinalReward() {
        Combination combination = new Combination(BigDecimal.valueOf(1), CombinationType.SAME_SYMBOLS, 3, CombinationGroup.SAME_SYMBOLS, null);
        SymbolWinCombination winCombination = new SymbolWinCombination("B", "same_symbol_3_times", combination);

        Reward reward = new Reward(List.of(winCombination), List.of("+1000"), symbols, BETTING_AMOUNT);

        assertThat(reward.getAmount()).isEqualTo(BigDecimal.valueOf(1300));
    }

    @ParameterizedTest
    @CsvSource({"5x,+500,2000", "10x,5x,15000", "+1000,+500,1800"})
    void shouldApplyMultipleBonusesCorrectly(String bonus1, String bonus2, int expectedAmount) {
        Combination combination = new Combination(BigDecimal.valueOf(1), CombinationType.SAME_SYMBOLS, 3, CombinationGroup.SAME_SYMBOLS, null);
        SymbolWinCombination winCombination = new SymbolWinCombination("B", "same_symbol_3_times", combination);

        Reward reward = new Reward(List.of(winCombination), List.of(bonus1, bonus2), symbols, BETTING_AMOUNT);

        assertThat(reward.getAmount()).isEqualTo(BigDecimal.valueOf(expectedAmount));
    }
}
