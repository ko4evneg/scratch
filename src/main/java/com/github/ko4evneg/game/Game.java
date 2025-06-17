package com.github.ko4evneg.game;

import com.github.ko4evneg.config.game.GameConfig;

import java.math.BigDecimal;
import java.util.List;

public class Game {
    private final GameConfig config;
    private final BigDecimal bettingAmount;
    private final WinCombinationCalculator winCombinationCalculator;

    public Game(GameConfig config, BigDecimal bettingAmount) {
        this.config = config;
        this.bettingAmount = bettingAmount;
        this.winCombinationCalculator = new WinCombinationCalculator(config.winCombinations(), config.symbols());
    }

    public GameResult start() {
        Matrix matrix = new Matrix(config.rows(), config.columns(), config.probabilities());
        List<SymbolWinCombination> winCombinations = winCombinationCalculator.calculateWinCombinations(matrix);
        String appliedBonusSymbol = null;
        if (!winCombinations.isEmpty()) {
            List<String> bonuses = matrix.getNonMissFieldBonuses();
            if (!bonuses.isEmpty()) {
                appliedBonusSymbol = String.join(",", bonuses);
            }
        }
        Reward reward = new Reward(winCombinations, matrix.getNonMissFieldBonuses(), config.symbols(), bettingAmount);
        return new GameResult(matrix, reward.getAmount(), appliedBonusSymbol, winCombinations);
    }
}
