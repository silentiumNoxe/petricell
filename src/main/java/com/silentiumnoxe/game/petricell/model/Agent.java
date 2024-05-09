package com.silentiumnoxe.game.petricell.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Agent {

    private Vector3 position;
    private Vector3 velocity;
    private Texture texture;
    private Rectangle mask;
}
