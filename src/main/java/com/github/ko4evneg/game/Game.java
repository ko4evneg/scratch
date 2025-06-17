package com.github.ko4evneg.game;

import com.github.ko4evneg.config.game.GameConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class Game {
    private final GameConfig config;
    private final BigDecimal bettingAmount;
    private final WinCombinationCalculator winCombinationCalculator;
    private final Random randomGenerator;

    public Game(GameConfig config, BigDecimal bettingAmount, Random randomGenerator) {
        this.config = config;
        this.bettingAmount = bettingAmount;
        this.randomGenerator = randomGenerator;
        this.winCombinationCalculator = new WinCombinationCalculator(config.winCombinations(), config.symbols());
    }

    public GameResult start() {
        Matrix matrix = new Matrix(config.rows(), config.columns(), config.probabilities(), randomGenerator);
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
