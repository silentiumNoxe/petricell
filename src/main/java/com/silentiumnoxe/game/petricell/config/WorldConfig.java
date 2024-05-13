package com.silentiumnoxe.game.petricell.config;

import com.badlogic.gdx.math.Circle;
import lombok.Data;

@Data
public class WorldConfig {

    private int width;
    private int height;
    private int radius;
    private float x;
    private float y;

    public WorldConfig(final int radius, final float x, final float y) {
        this.width = this.height = radius * 2;
        this.radius = radius;
        this.x = x;
        this.y = y;
    }

    public Circle getShape() {
        return new Circle(x, y, radius);
    }
}
