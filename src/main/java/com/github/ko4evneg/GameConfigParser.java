package com.github.ko4evneg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.github.ko4evneg.config.game.GameConfig;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class GameConfigParser {
    private static final ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    public static GameConfig parse(Path path) {
        try {
            byte[] rawConfig = Files.readAllBytes(path);
            return mapper.readValue(rawConfig, GameConfig.class);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot parse config file", e);
        }
    }
}
