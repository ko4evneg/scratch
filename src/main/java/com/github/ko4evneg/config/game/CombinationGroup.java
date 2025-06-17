package com.github.ko4evneg.config.game;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CombinationGroup {
    @JsonProperty("same_symbols")
    SAME_SYMBOLS,
    @JsonProperty("horizontally_linear_symbols")
    HORIZONTALLY_LINEAR_SYMBOLS,
    @JsonProperty("vertically_linear_symbols")
    VERTICALLY_LINEAR_SYMBOLS,
    @JsonProperty("ltr_diagonally_linear_symbols")
    LTR_DIAGONALLY_LINEAR_SYMBOLS,
    @JsonProperty("rtl_diagonally_linear_symbols")
    RTL_DIAGONALLY_LINEAR_SYMBOLS,
}
