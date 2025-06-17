package com.github.ko4evneg;

import com.github.ko4evneg.config.AppConfig;
import com.github.ko4evneg.config.game.GameConfig;
import com.github.ko4evneg.game.Game;

public class Main {
    public static void main(String... args) {
        AppConfig appConfig = new AppConfig(args);
        GameConfig gameConfig = ScratchSerializer.deserialize(appConfig.getConfigPath());
        //validate probabilities
        Game game = new Game(gameConfig);
        GameResult result = game.start();
        String textResult = ScratchSerializer.serialize(result);
        System.out.println(textResult);
    }
}
