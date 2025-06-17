package com.github.ko4evneg.game;

import com.github.ko4evneg.config.game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WinCombinationCalculatorTest {
    private Matrix matrix;
    private WinCombinationCalculator calculator;

    @BeforeEach
    void setUp() {
        matrix = mock(Matrix.class);

        Map<String, Symbol> symbols = Map.of(
                "A", new Symbol(SymbolType.STANDARD, BigDecimal.valueOf(5), null, null),
                "B", new Symbol(SymbolType.STANDARD, BigDecimal.valueOf(3), null, null),
                "C", new Symbol(SymbolType.STANDARD, BigDecimal.valueOf(2.5), null, null),
                "10x", new Symbol(SymbolType.BONUS, BigDecimal.valueOf(10), null, BonusImpact.MULTIPLY_REWARD)
        );

        Map<String, Combination> winCombinations = Map.of(
                "same_symbol_3_times", new Combination(
                        BigDecimal.valueOf(1),
                        CombinationType.SAME_SYMBOLS,
                        3,
                        CombinationGroup.SAME_SYMBOLS,
                        null
                ),
                "same_symbol_4_times", new Combination(
                        BigDecimal.valueOf(1.5),
                        CombinationType.SAME_SYMBOLS,
                        4,
                        CombinationGroup.SAME_SYMBOLS,
                        null
                ),
                "same_symbol_5_times", new Combination(
                        BigDecimal.valueOf(2),
                        CombinationType.SAME_SYMBOLS,
                        5,
                        CombinationGroup.SAME_SYMBOLS,
                        null
                )
        );

        calculator = new WinCombinationCalculator(winCombinations, symbols);
    }

    @Test
    void shouldReturnNoWinCombinationsWhenNoSymbolMeetsMinimumCount() {
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 2, "B", 1, "C", 1));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnSingleCombinationWhenSymbolMeetsExactCount() {
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 3, "B", 2));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);
        SymbolWinCombination actualCombination = result.getFirst();

        assertThat(result).hasSize(1);
        assertThat(actualCombination.symbol()).isEqualTo("A");
        assertThat(actualCombination.combinationName()).isEqualTo("same_symbol_3_times");
        assertThat(actualCombination.getRewardMultiplier()).isEqualTo(BigDecimal.valueOf(1));
    }

    @Test
    void shouldReturnHighestCombinationWhenSymbolQualifiesForMultiple() {
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 5, "B", 2));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);
        SymbolWinCombination actualCombination = result.getFirst();

        assertThat(result).hasSize(1);
        assertThat(actualCombination.symbol()).isEqualTo("A");
        assertThat(actualCombination.combinationName()).isEqualTo("same_symbol_5_times");
        assertThat(actualCombination.getRewardMultiplier()).isEqualTo(BigDecimal.valueOf(2));
    }

    @Test
    void shouldIgnoreBonusSymbolsInCombinationDetection() {
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 3, "10x", 4));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);
        SymbolWinCombination actualCombination = result.getFirst();

        assertThat(result).hasSize(1);
        assertThat(actualCombination.symbol()).isEqualTo("A");
    }

    @Test
    void shouldSelectBestCombinationFromSameGroupInMultipleSymbols() {
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 3, "B", 5));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);
        SymbolWinCombination actualCombination = result.getFirst();

        assertThat(result).hasSize(1);
        assertThat(actualCombination.symbol()).isEqualTo("B");
        assertThat(actualCombination.combinationName()).isEqualTo("same_symbol_5_times");
        assertThat(actualCombination.getRewardMultiplier()).isEqualTo(BigDecimal.valueOf(2));
    }

    @Test
    void shouldUseAtLeastCountSemantics() {
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 7));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);
        SymbolWinCombination actualCombination = result.getFirst();

        assertThat(result).hasSize(1);
        assertThat(actualCombination.symbol()).isEqualTo("A");
        assertThat(actualCombination.combinationName()).isEqualTo("same_symbol_5_times");
    }
}
