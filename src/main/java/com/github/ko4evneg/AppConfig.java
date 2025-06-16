package com.github.ko4evneg;

import lombok.Getter;

import java.math.BigDecimal;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Getter
public class AppConfig {
    private static final String CONFIG_PATH_KEY = "--config";
    private static final String BETTING_AMOUNT_KEY = "--betting-amount";

    private final Path configPath;
    private final BigDecimal bettingAmount;

    public AppConfig(String... args) {
        validateArgumentsCount(args);

        String rawConfigPath = null;
        String rawBettingAmount = null;
        for (int i = 0; i < args.length; i += 2) {
            String rawKey = args[i];
            String rawValue = args[i + 1];

            switch (rawKey) {
                case BETTING_AMOUNT_KEY -> rawBettingAmount = rawValue;
                case CONFIG_PATH_KEY -> rawConfigPath = rawValue;
                default -> throw new IllegalArgumentException("Unsupported argument with key: " + rawKey);
            }
        }

        validateRequiredArgumentsProvided(rawConfigPath, rawBettingAmount);

        try {
            configPath = Path.of(rawConfigPath);
            bettingAmount = new BigDecimal(rawBettingAmount);
        } catch (InvalidPathException ex) {
            throw new IllegalArgumentException("Invalid path provided in argument: " + CONFIG_PATH_KEY);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid number provided in argument: " + BETTING_AMOUNT_KEY);
        }
    }

    private void validateArgumentsCount(String[] args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("Arguments must be provided in pairs: --key value");
        }
    }

    private void validateRequiredArgumentsProvided(String rawConfigPath, String rawBettingAmount) {
        if (rawConfigPath == null) {
            throw new IllegalArgumentException("Missing argument " + CONFIG_PATH_KEY);
        }
        if (rawBettingAmount == null) {
            throw new IllegalArgumentException("Missing argument " + BETTING_AMOUNT_KEY);
        }
    }
}
