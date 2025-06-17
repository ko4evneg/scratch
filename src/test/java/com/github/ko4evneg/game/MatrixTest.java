package com.github.ko4evneg.game;

import com.github.ko4evneg.config.game.Probabilities;
import com.github.ko4evneg.config.game.SymbolProbability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class MatrixTest {
    private final Random mockRandom = mock(Random.class);
    private final Map<String, Integer> bonusSymbolProbability = new LinkedHashMap<>();
    private final Map<String, Integer> standardSymbolsProbability = new LinkedHashMap<>();

    @BeforeEach
    void setUp() {
        doReturn(0.9).when(mockRandom).nextDouble();
        standardSymbolsProbability.put("A", 1);
        standardSymbolsProbability.put("B", 2);
        standardSymbolsProbability.put("C", 3);
        bonusSymbolProbability.put("+50", 1);
        bonusSymbolProbability.put("*2", 2);
        bonusSymbolProbability.put("MISS", 3);
    }

    @Test
    void shouldCreateMatrixWithStandardSymbolsWhenBonusRandomFails() {
        Probabilities probabilities = new Probabilities(
                createSymbolProbabilities(standardSymbolsProbability), new SymbolProbability(null, null, Map.of()));
        doReturn(0, 1, 2, 3, 4, 5).when(mockRandom).nextInt(6);

        Matrix matrix = new Matrix(2, 3, probabilities, mockRandom);
        String[][] actualField = matrix.getField();
        List<String> actualSymbols = Arrays.stream(actualField).flatMap(Arrays::stream).toList();

        assertThat(actualField).hasDimensions(2, 3);
        assertThat(actualSymbols).containsExactlyInAnyOrder("A", "B", "B", "C", "C", "C");
    }

    @Test
    void shouldCreateMatrixWithBonusSymbolsWhenBonusRandomSucceeds() {
        Probabilities probabilities = new Probabilities(List.of(
                new SymbolProbability(0, 0, standardSymbolsProbability)), new SymbolProbability(null, null, bonusSymbolProbability));
        doReturn(0.0999).when(mockRandom).nextDouble();
        doReturn(0, 1, 2, 3, 4, 5).when(mockRandom).nextInt(6);

        Matrix matrix = new Matrix(2, 3, probabilities, mockRandom);
        String[][] actualField = matrix.getField();
        List<String> actualSymbols = Arrays.stream(actualField).flatMap(Arrays::stream).toList();

        assertThat(actualField).hasDimensions(2, 3);
        assertThat(actualSymbols).containsExactlyInAnyOrder("+50", "*2", "*2", "MISS", "MISS", "MISS");
    }

    @Test
    void shouldFillCellsWithFirstSymbolWhenNotDefinedInSymbols() {
        Probabilities probabilities = new Probabilities(
                List.of(new SymbolProbability(0, 0, Map.of("X", 1))), new SymbolProbability(null, null, Map.of()));
        doReturn(0, 1, 2, 3, 4, 5, 6, 7, 8).when(mockRandom).nextInt(9);

        Matrix matrix = new Matrix(3, 3, probabilities, mockRandom);
        String[][] actualField = matrix.getField();
        List<String> actualSymbols = Arrays.stream(actualField).flatMap(Arrays::stream).toList();

        assertThat(actualField).hasDimensions(3, 3);
        assertThat(actualSymbols).containsExactlyInAnyOrder("X", "X", "X", "X", "X", "X", "X", "X", "X");

    }

    @Test
    void shouldCountSymbolQuantitiesCorrectly() {
        Probabilities probabilities = new Probabilities(
                createSymbolProbabilities(standardSymbolsProbability), new SymbolProbability(null, null, bonusSymbolProbability));
        doReturn(0.2, 0.2, 0.2, 0.2, 0.01).when(mockRandom).nextDouble();
        doReturn(0, 6, 4, 2, 3).when(mockRandom).nextInt(6);

        Matrix matrix = new Matrix(2, 3, probabilities, mockRandom);

        assertThat(matrix.getQuantityPerSymbol())
                .contains(
                        entry("A", 2),
                        entry("B", 1),
                        entry("C", 1),
                        entry("MISS", 2)
                );

    }

    @Test
    void shouldDetectBonusSymbolsExcludingMiss() {
        Probabilities probabilities = new Probabilities(List.of(
                new SymbolProbability(0, 0, standardSymbolsProbability)), new SymbolProbability(null, null, bonusSymbolProbability));
        doReturn(0.0999).when(mockRandom).nextDouble();
        doReturn(0, 1, 2, 3, 4, 5).when(mockRandom).nextInt(6);

        Matrix matrix = new Matrix(2, 3, probabilities, mockRandom);

        assertThat(matrix.getNonMissFieldBonuses()).containsExactlyInAnyOrder("+50", "*2", "*2");
    }

    private List<SymbolProbability> createSymbolProbabilities(Map<String, Integer> probabilityMap) {
        return List.of(
                new SymbolProbability(0, 0, probabilityMap),
                new SymbolProbability(0, 1, probabilityMap),
                new SymbolProbability(0, 2, probabilityMap),
                new SymbolProbability(1, 0, probabilityMap),
                new SymbolProbability(1, 1, probabilityMap),
                new SymbolProbability(1, 2, probabilityMap)
        );
    }
}
