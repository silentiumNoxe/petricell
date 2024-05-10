package com.silentiumnoxe.game.petricell.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Agent {

    private final UUID id = UUID.randomUUID();
    private Vector2 position;
    private float velocity;
    private float angle;
    private Texture texture;
    private Rectangle mask;
    private Sector sector;

    private int size;

    public Rectangle getMask() {
        return new Rectangle(position.x, position.y, mask.width, mask.height);
    }

    public void setAngle(final float angle) {
        var x = angle;
        if (x > 360) {
            x = x % 360;
        }
        if (x < 0) {
            x = x % -360;
        }

        this.angle = x;
    }

    public void setVelocity(final float velocity) {
        if (velocity < 0) {
            this.velocity = -velocity;
            setAngle(angle + 180);
        }
    }
}
