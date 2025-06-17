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
        Map<CombinationGroup, List<WinCombination>> combinationByGroup = winCombinations.entrySet().stream()
                .collect(Collectors.groupingBy(e -> e.getValue().group(),
                        Collectors.mapping(e -> new WinCombination(e.getKey(), e.getValue()), Collectors.toList())));
        List<SymbolWinCombination> applicableCombinations = new ArrayList<>();
        combinationByGroup.forEach((group, combinations) -> {
            switch (group) {
                case SAME_SYMBOLS -> getSameSymbolCombination(combinations, matrix).ifPresent(applicableCombinations::add);
            }
        });
        return applicableCombinations;
    }

    private Optional<SymbolWinCombination> getSameSymbolCombination(List<WinCombination> combinations, Matrix matrix) {
        Map<String, Integer> quantityPerStandardSymbol = matrix.getQuantityPerSymbol().entrySet().stream()
                .filter(this::isStandardSymbol)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<SymbolWinCombination> sameSymbolWinCombinations = new ArrayList<>();
        quantityPerStandardSymbol
                .forEach((symbol, count) -> combinations.stream()
                        .filter(winComb -> winComb.combination().when() == CombinationType.SAME_SYMBOLS)
                        .filter(winComb -> winComb.combination().count() <= count)
                        .map(comb -> new SymbolWinCombination(symbol, comb))
                        .forEach(sameSymbolWinCombinations::add)
                );
        return sameSymbolWinCombinations.stream()
                .max(Comparator.comparing(winComb -> winComb.combination().combination().rewardMultiplier()));
    }

    private boolean isStandardSymbol(Map.Entry<String, Integer> entry) {
        Symbol symbol = symbolsByName.get(entry.getKey());
        return symbol.type() == SymbolType.STANDARD;
    }
}
