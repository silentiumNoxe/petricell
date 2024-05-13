package com.silentiumnoxe.game.petricell.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Renderer {

    void render(final Batch batch, final float delta);

    default void render(final ShapeRenderer shapeRenderer, final float delta) {

    }
}
