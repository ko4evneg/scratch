package com.github.ko4evneg.game;

import com.github.ko4evneg.config.game.*;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WinCombinationCalculator {
    private final Map<String, Combination> winCombinations;
    private final Map<String, Symbol> symbolsByName;

    public List<SymbolWinCombination> calculateWinCombinations(Matrix matrix) {
        Map<CombinationGroup, Map<String, Combination>> combinationByGroup = winCombinations.entrySet().stream()
                .collect(Collectors.groupingBy(e -> e.getValue().group(),
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        List<SymbolWinCombination> applicableCombinations = new ArrayList<>();
        combinationByGroup.forEach((group, combinations) -> {
            switch (group) {
                case SAME_SYMBOLS ->
                        getSameSymbolCombination(combinations, matrix).ifPresent(applicableCombinations::add);
                case HORIZONTALLY_LINEAR_SYMBOLS, VERTICALLY_LINEAR_SYMBOLS,
                     LTR_DIAGONALLY_LINEAR_SYMBOLS, RTL_DIAGONALLY_LINEAR_SYMBOLS ->
                        getLinearSymbolCombination(combinations, matrix).ifPresent(applicableCombinations::add);
            }
        });
        return applicableCombinations;
    }

    private Optional<SymbolWinCombination> getSameSymbolCombination(Map<String, Combination> combinations, Matrix matrix) {
        Map<String, Integer> quantityPerStandardSymbol = matrix.getQuantityPerSymbol().entrySet().stream()
                .filter(this::isStandardSymbol)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<SymbolWinCombination> sameSymbolWinCombinations = new ArrayList<>();
        quantityPerStandardSymbol
                .forEach((symbol, count) -> combinations.entrySet().stream()
                        .filter(combEntry -> combEntry.getValue().when() == CombinationType.SAME_SYMBOLS)
                        .filter(combEntry -> combEntry.getValue().count() <= count)
                        .map(combEntry -> new SymbolWinCombination(symbol, combEntry.getKey(), combEntry.getValue()))
                        .forEach(sameSymbolWinCombinations::add)
                );
        return sameSymbolWinCombinations.stream()
                .max(Comparator.comparing(SymbolWinCombination::getRewardMultiplier));
    }

    private Optional<SymbolWinCombination> getLinearSymbolCombination(Map<String, Combination> combinations, Matrix matrix) {
        Set<String> standardSymbols = symbolsByName.entrySet().stream()
                .filter(entry -> entry.getValue().type() == SymbolType.STANDARD)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        List<SymbolWinCombination> linearWinCombinations = new ArrayList<>();
        for (String symbol : standardSymbols) {
            for (Map.Entry<String, Combination> combEntry : combinations.entrySet()) {
                Combination combination = combEntry.getValue();
                if (combination.when() == CombinationType.LINEAR_SYMBOLS && hasLinearWin(symbol, combination, matrix)) {
                    linearWinCombinations.add(new SymbolWinCombination(symbol, combEntry.getKey(), combination));
                }
            }
        }

        return linearWinCombinations.stream()
                .max(Comparator.comparing(SymbolWinCombination::getRewardMultiplier));
    }

    private boolean hasLinearWin(String symbol, Combination combination, Matrix matrix) {
        for (String[] coveredArea : combination.coveredAreas()) {
            boolean allPositionsMatch = true;
            for (String positionStr : coveredArea) {
                String[] parts = positionStr.split(":");
                int row = Integer.parseInt(parts[0]);
                int column = Integer.parseInt(parts[1]);
                if (!symbol.equals(matrix.getField()[row][column])) {
                    allPositionsMatch = false;
                    break;
                }
            }
            if (allPositionsMatch) {
                return true;
            }
        }
        return false;
    }

    private boolean isStandardSymbol(Map.Entry<String, Integer> entry) {
        Symbol symbol = symbolsByName.get(entry.getKey());
        return symbol.type() == SymbolType.STANDARD;
    }
}
