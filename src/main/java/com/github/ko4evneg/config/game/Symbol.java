package com.github.ko4evneg.config.game;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Symbol {
    private String type;
    private BigDecimal rewardMultiplier;
    private BigDecimal extra;
    private String impact;
}
