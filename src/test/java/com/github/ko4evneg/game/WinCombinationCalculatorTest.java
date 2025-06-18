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
                ),
                "horizontal_line", new Combination(
                        BigDecimal.valueOf(2),
                        CombinationType.LINEAR_SYMBOLS,
                        3,
                        CombinationGroup.HORIZONTALLY_LINEAR_SYMBOLS,
                        new String[][]{{"0:0", "0:1", "0:2"}, {"1:0", "1:1", "1:2"}, {"2:0", "2:1", "2:2"}}
                ),
                "vertical_line", new Combination(
                        BigDecimal.valueOf(2),
                        CombinationType.LINEAR_SYMBOLS,
                        3,
                        CombinationGroup.VERTICALLY_LINEAR_SYMBOLS,
                        new String[][]{{"0:0", "1:0", "2:0"}, {"0:1", "1:1", "2:1"}, {"0:2", "1:2", "2:2"}}
                ),
                "diagonal_ltr", new Combination(
                        BigDecimal.valueOf(5),
                        CombinationType.LINEAR_SYMBOLS,
                        3,
                        CombinationGroup.LTR_DIAGONALLY_LINEAR_SYMBOLS,
                        new String[][]{{"0:0", "1:1", "2:2"}}
                ),
                "diagonal_rtl", new Combination(
                        BigDecimal.valueOf(5),
                        CombinationType.LINEAR_SYMBOLS,
                        3,
                        CombinationGroup.RTL_DIAGONALLY_LINEAR_SYMBOLS,
                        new String[][]{{"0:2", "1:1", "2:0"}}
                )
        );

        calculator = new WinCombinationCalculator(winCombinations, symbols);
    }

    @Test
    void shouldReturnNoWinCombinationsWhenNoSymbolMeetsMinimumCount() {
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 2, "B", 1, "C", 1));
        when(matrix.getField()).thenReturn(new String[3][3]);

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnSingleCombinationWhenSymbolMeetsExactCount() {
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 3, "B", 2));
        when(matrix.getField()).thenReturn(new String[3][3]);

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
        when(matrix.getField()).thenReturn(new String[3][3]);

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
        when(matrix.getField()).thenReturn(new String[3][3]);

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);
        SymbolWinCombination actualCombination = result.getFirst();

        assertThat(result).hasSize(1);
        assertThat(actualCombination.symbol()).isEqualTo("A");
    }

    @Test
    void shouldSelectBestCombinationFromSameGroupInMultipleSymbols() {
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 3, "B", 5));
        when(matrix.getField()).thenReturn(new String[3][3]);

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
        when(matrix.getField()).thenReturn(new String[3][3]);

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);
        SymbolWinCombination actualCombination = result.getFirst();

        assertThat(result).hasSize(1);
        assertThat(actualCombination.symbol()).isEqualTo("A");
        assertThat(actualCombination.combinationName()).isEqualTo("same_symbol_5_times");
    }

    @Test
    void shouldDetectHorizontalLinearWin() {
        String[][] field = {
                {"A", "A", "A"},
                {"B", "C", "B"},
                {"C", "B", "C"}
        };
        when(matrix.getField()).thenReturn(field);
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 3, "B", 3, "C", 3));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);

        assertThat(result).hasSize(2);
        assertThat(result).anySatisfy(combination -> {
            assertThat(combination.symbol()).isEqualTo("A");
            assertThat(combination.combinationName()).isEqualTo("horizontal_line");
            assertThat(combination.getRewardMultiplier()).isEqualTo(BigDecimal.valueOf(2));
        });
    }

    @Test
    void shouldDetectVerticalLinearWin() {
        String[][] field = {
                {"A", "B", "C"},
                {"A", "C", "B"},
                {"A", "B", "C"}
        };
        when(matrix.getField()).thenReturn(field);
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 3, "B", 3, "C", 3));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);

        assertThat(result).hasSize(2);
        assertThat(result).anySatisfy(combination -> {
            assertThat(combination.symbol()).isEqualTo("A");
            assertThat(combination.combinationName()).isEqualTo("vertical_line");
            assertThat(combination.getRewardMultiplier()).isEqualTo(BigDecimal.valueOf(2));
        });
    }

    @Test
    void shouldDetectLeftToRightDiagonalWin() {
        String[][] field = {
                {"A", "B", "C"},
                {"B", "A", "C"},
                {"C", "B", "A"}
        };
        when(matrix.getField()).thenReturn(field);
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 3, "B", 3, "C", 3));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);

        assertThat(result).hasSize(2);
        assertThat(result).anySatisfy(combination -> {
            assertThat(combination.symbol()).isEqualTo("A");
            assertThat(combination.combinationName()).isEqualTo("diagonal_ltr");
            assertThat(combination.getRewardMultiplier()).isEqualTo(BigDecimal.valueOf(5));
        });
    }

    @Test
    void shouldDetectRightToLeftDiagonalWin() {
        String[][] field = {
                {"B", "C", "A"},
                {"C", "A", "B"},
                {"A", "B", "C"}
        };
        when(matrix.getField()).thenReturn(field);
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 3, "B", 3, "C", 3));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);

        assertThat(result).hasSize(2);
        assertThat(result).anySatisfy(combination -> {
            assertThat(combination.symbol()).isEqualTo("A");
            assertThat(combination.combinationName()).isEqualTo("diagonal_rtl");
            assertThat(combination.getRewardMultiplier()).isEqualTo(BigDecimal.valueOf(5));
        });
    }

    @Test
    void shouldReturnAllCombinationsInDifferentGroupsWhenMultipleDiagonalsMatch() {
        String[][] field = {
                {"A", "B", "A"},
                {"B", "A", "B"},
                {"A", "B", "A"}
        };
        when(matrix.getField()).thenReturn(field);
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 5, "B", 4));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);

        assertThat(result).hasSize(3);
        assertThat(result).anySatisfy(combination -> {
            assertThat(combination.symbol()).isEqualTo("A");
            assertThat(combination.combinationName()).isEqualTo("diagonal_ltr");
        });
        assertThat(result).anySatisfy(combination -> {
            assertThat(combination.symbol()).isEqualTo("A");
            assertThat(combination.combinationName()).isEqualTo("diagonal_rtl");
        });
        assertThat(result).anySatisfy(combination -> {
            assertThat(combination.symbol()).isEqualTo("A");
            assertThat(combination.combinationName()).isEqualTo("same_symbol_5_times");
        });
    }

    @Test
    void shouldReturnNoLinearWinWhenPatternNotComplete() {
        String[][] field = {
                {"A", "A", "B"},
                {"B", "C", "B"},
                {"C", "B", "C"}
        };
        when(matrix.getField()).thenReturn(field);
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("A", 2, "B", 4, "C", 3));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);

        assertThat(result).anySatisfy(combination -> {
            assertThat(combination.symbol()).isEqualTo("B");
            assertThat(combination.combinationName()).isEqualTo("same_symbol_4_times");
        });
    }

    @Test
    void shouldIgnoreBonusSymbolsInLinearCombinations() {
        String[][] field = {
                {"10x", "10x", "10x"},
                {"A", "B", "C"},
                {"C", "B", "A"}
        };
        when(matrix.getField()).thenReturn(field);
        when(matrix.getQuantityPerSymbol()).thenReturn(Map.of("10x", 3, "A", 2, "B", 2, "C", 2));

        List<SymbolWinCombination> result = calculator.calculateWinCombinations(matrix);

        assertThat(result).isEmpty();
    }
}
