package com.github.ko4evneg;

import com.github.ko4evneg.config.AppConfig;
import com.github.ko4evneg.config.game.GameConfig;

public class Main {
    public static void main(String... args) {
        AppConfig appConfig = new AppConfig(args);
        GameConfig gameConfig = GameConfigParser.parse(appConfig.getConfigPath());
        Game game = new Game();
    }
}
