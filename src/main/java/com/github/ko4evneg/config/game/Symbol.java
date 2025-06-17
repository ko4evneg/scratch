package com.github.ko4evneg.config.game;

import java.math.BigDecimal;

public record Symbol(
        SymbolType type,
        BigDecimal rewardMultiplier,
        BigDecimal extra,
        BonusImpact impact
) {
}
