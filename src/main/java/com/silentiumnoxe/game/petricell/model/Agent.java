package com.silentiumnoxe.game.petricell.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Agent {

    private final UUID id = UUID.randomUUID();
    private Vector3 position;
    private Vector3 velocity;
    private Texture texture;
    private Rectangle mask;
    private Sector sector;

    public Rectangle getMask() {
        return new Rectangle(position.x, position.y, mask.width, mask.height);
    }
}
