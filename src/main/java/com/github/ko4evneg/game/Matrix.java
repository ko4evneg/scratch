package com.github.ko4evneg.game;

import com.github.ko4evneg.config.game.Probabilities;
import com.github.ko4evneg.config.game.SymbolProbability;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Matrix {
    private final Random random = new Random();
    private final String[][] field;

    public Matrix(int rows, int columns, Probabilities probabilities) {
        field = new String[rows][columns];

        SymbolProbability bonusProbability = probabilities.getBonusSymbols();
        List<SymbolProbability> standardSymbols = probabilities.getStandardSymbols();
        List<SymbolProbability> standardProbabilities = getAugmentedStandardProbabilities(standardSymbols);

        fillMatrix(bonusProbability, standardProbabilities);
    }

    private List<SymbolProbability> getAugmentedStandardProbabilities(List<SymbolProbability> standardProbabilities) {
        Set<Cell> filledCells = standardProbabilities.stream()
                .map(Cell::new)
                .collect(Collectors.toSet());
        SymbolProbability referenceProbability = standardProbabilities.getFirst();

        List<SymbolProbability> augmentedProbabilities = new ArrayList<>(standardProbabilities);
        for (int row = 0; row < field.length; row++) {
            for (int column = 0; column < field[0].length; column++) {
                Cell checkedCell = new Cell(row, column);
                if (!filledCells.contains(checkedCell)) {
                    SymbolProbability augmentedProbability = new SymbolProbability(row, column, referenceProbability.getSymbols());
                    augmentedProbabilities.add(augmentedProbability);
                }
            }
        }

        return augmentedProbabilities;
    }

    private void fillMatrix(SymbolProbability bonusSymbolsProbability, List<SymbolProbability> standardSymbolsProbabilities) {
        Map<String, Integer> bonusSymbolsProbabilities = bonusSymbolsProbability.getSymbols();

        for (SymbolProbability symbolProbability : standardSymbolsProbabilities) {
            Map<String, Integer> standardSymbolProbability = symbolProbability.getSymbols();
            Map<String, Integer> allSymbolsProbabilities = new HashMap<>(bonusSymbolsProbabilities);
            allSymbolsProbabilities.putAll(standardSymbolProbability);

            int totalSymbolsWeight = allSymbolsProbabilities.values().stream().mapToInt(Integer::intValue).sum();
            int randomWeight = this.random.nextInt(totalSymbolsWeight);
            int weight = 0;
            for (Map.Entry<String, Integer> entry : allSymbolsProbabilities.entrySet()) {
                weight += entry.getValue();
                if (weight > randomWeight) {
                    String symbolName = entry.getKey();
                    field[symbolProbability.getRow()][symbolProbability.getColumn()] = symbolName;
                    break;
                }
            }
        }
    }

    private static class Cell {
        private final int row;
        private final int column;

        public Cell(SymbolProbability symbolProbability) {
            this.row = symbolProbability.getRow();
            this.column = symbolProbability.getColumn();
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
