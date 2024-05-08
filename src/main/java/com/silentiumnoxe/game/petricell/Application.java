package com.silentiumnoxe.game.petricell;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import lombok.SneakyThrows;

public class Application {

    @SneakyThrows
    public static void main(final String[] args) {

        try {
            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            config.setTitle("Petricell");
            config.setWindowedMode(1280, 720);
            config.useVsync(true);
            config.setForegroundFPS(60);
            new Lwjgl3Application(new GameGame(), config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
