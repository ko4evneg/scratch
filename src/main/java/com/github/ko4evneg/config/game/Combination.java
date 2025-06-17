package com.github.ko4evneg.config.game;

import java.math.BigDecimal;

public record Combination(BigDecimal rewardMultiplier, String when, Integer count, CombinationGroup group,
                          String[][] coveredAreas) {
}
