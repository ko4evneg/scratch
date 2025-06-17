package com.github.ko4evneg.config.game;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Combination {
    private BigDecimal rewardMultiplier;
    private String when;
    private Integer count;
    private String group;
    private String[][] coveredAreas;
}
