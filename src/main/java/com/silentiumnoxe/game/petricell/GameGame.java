package com.silentiumnoxe.game.petricell;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.silentiumnoxe.game.petricell.screen.MainMenuScreen;
import com.silentiumnoxe.game.petricell.util.ScreenSelector;

public class GameGame extends Game {

    public GameGame() {
        ScreenSelector.getInstance().onchange(this::onScreenChanged);
    }

    @Override
    public void create() {

        var ss = ScreenSelector.getInstance();
        ss.add("main-menu", new MainMenuScreen());

        ss.choose("main-menu");
    }

    private void onScreenChanged(final Screen screen) {
        if (screen == null) {
            return;
        }

        this.setScreen(screen);
    }
}
