package com.github.ko4evneg;

import com.github.ko4evneg.config.AppConfig;
import com.github.ko4evneg.config.game.GameConfig;
import com.github.ko4evneg.game.Game;
import com.github.ko4evneg.game.GameResult;

import java.util.Random;

public class ScratchGame {
    public static void main(String... args) {
        AppConfig appConfig = new AppConfig(args);
        GameConfig gameConfig = ScratchSerializer.deserialize(appConfig.getConfigPath());
        //TODO validate probabilities; first cell symbol; win combinations count < overall
        Game game = new Game(gameConfig, appConfig.getBettingAmount(), new Random());
        GameResult result = game.start();
        String textResult = ScratchSerializer.serialize(result);
        System.out.println(textResult);
    }
}
