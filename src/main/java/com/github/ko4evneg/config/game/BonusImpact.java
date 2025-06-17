package com.github.ko4evneg.config.game;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BonusImpact {
    @JsonProperty("multiply_reward")
    MULTIPLY_REWARD,
    @JsonProperty("extra_bonus")
    EXTRA_BONUS,
    @JsonProperty("miss")
    MISS,
}
