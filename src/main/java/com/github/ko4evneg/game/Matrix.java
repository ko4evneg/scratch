package com.github.ko4evneg.game;

import com.github.ko4evneg.config.game.Probabilities;
import com.github.ko4evneg.config.game.SymbolProbability;
import lombok.Getter;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

@Getter
public class Matrix {
    private static final String MISS_BONUS = "MISS";
    private static final double BONUS_PROBABILITY_PER_CELL = 0.10;

    private final RandomGenerator randomGenerator;
    private final SymbolProbability bonusProbability;
    private final List<SymbolProbability> augmentedStandardProbabilities;
    private final String[][] field;

    public Matrix(int rows, int columns, Probabilities probabilities, Random randomGenerator) {
        this.randomGenerator = randomGenerator;
        field = new String[rows][columns];
        bonusProbability = probabilities.bonusSymbols();
        List<SymbolProbability> standardProbabilities = probabilities.standardSymbols();
        augmentedStandardProbabilities = getAugmentedStandardProbabilities(standardProbabilities);
        fillMatrix();
    }

    private List<SymbolProbability> getAugmentedStandardProbabilities(List<SymbolProbability> standardProbabilities) {
        Set<Cell> filledCells = standardProbabilities.stream()
                .map(Cell::new)
                .collect(Collectors.toSet());
        SymbolProbability referenceProbability = standardProbabilities.getFirst();

        List<SymbolProbability> augmentedProbabilities = new ArrayList<>(standardProbabilities);
        traverseField((row, column) -> {
            Cell checkedCell = new Cell(row, column);
            if (!filledCells.contains(checkedCell)) {
                SymbolProbability augmentedProbability = new SymbolProbability(row, column, referenceProbability.symbols());
                augmentedProbabilities.add(augmentedProbability);
            }
        });

        return augmentedProbabilities;
    }

    private void fillMatrix() {
        Map<String, Integer> bonusSymbolsProbabilities = bonusProbability.symbols();

        for (SymbolProbability symbolProbability : augmentedStandardProbabilities) {
            String selectedSymbol;
            if (randomGenerator.nextDouble() < BONUS_PROBABILITY_PER_CELL) {
                selectedSymbol = selectSymbolFromProbabilities(bonusSymbolsProbabilities);
            } else {
                Map<String, Integer> standardSymbolProbability = symbolProbability.symbols();
                selectedSymbol = selectSymbolFromProbabilities(standardSymbolProbability);
            }

            field[symbolProbability.row()][symbolProbability.column()] = selectedSymbol;
        }
    }

    private String selectSymbolFromProbabilities(Map<String, Integer> symbolProbabilities) {
        int totalWeight = symbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();
        int randomWeight = randomGenerator.nextInt(totalWeight);
        int weight = 0;

        for (Map.Entry<String, Integer> entry : symbolProbabilities.entrySet()) {
            weight += entry.getValue();
            if (weight > randomWeight) {
                return entry.getKey();
            }
        }

        return symbolProbabilities.keySet().iterator().next();
    }

    public Map<String, Integer> getQuantityPerSymbol() {
        Map<String, Integer> quantityPerSymbol = new HashMap<>();
        traverseField((row, column) ->
                quantityPerSymbol.merge(field[row][column], 1, (oldVal, val) -> oldVal + 1));
        return quantityPerSymbol;
    }

    public List<String> getNonMissFieldBonuses() {
        List<String> appliedBonuses = new ArrayList<>();
        Set<String> bonusSymbols = new HashSet<>(bonusProbability.symbols().keySet());
        traverseField((row, column) -> {
            String currentSymbol = field[row][column];
            if (bonusSymbols.contains(currentSymbol) && !MISS_BONUS.equals(currentSymbol)) {
                appliedBonuses.add(currentSymbol);
            }
        });
        return appliedBonuses;
    }

    private void traverseField(BiConsumer<Integer, Integer> consumer) {
        for (int row = 0; row < field.length; row++) {
            for (int column = 0; column < field[0].length; column++) {
                consumer.accept(row, column);
            }
        }
    }

    private static class Cell {
        private final int row;
        private final int column;

        public Cell(SymbolProbability symbolProbability) {
            this.row = symbolProbability.row();
            this.column = symbolProbability.column();
        }

        public Cell(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return row == cell.row && column == cell.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }
}
