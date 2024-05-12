package com.silentiumnoxe.game.petricell.model;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

public interface Masked {

    Shape2D getMask();

    default boolean isCircle() {
        return getMask() instanceof Circle;
    }

    default boolean isRect() {
        return getMask() instanceof Rectangle;
    }
}
