package com.silentiumnoxe.game.petricell.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

public abstract class BaseScreen implements Screen {

    protected final Stage stage;

    public BaseScreen() {
        this.stage = new Stage();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(Color.BLACK);

        stage.act();
        preRender(delta);
        stage.draw();
        postRender(delta);
    }

    public void preRender(final float delta) {

    }

    public void postRender(final float delta) {

    }

    @Override
    public void resize(final int width, final int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
