package com.silentiumnoxe.game.petricell.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Agent extends Actor {

    private final UUID id = UUID.randomUUID();
    private Vector2 position;
    private float velocity;
    private float angle;
    private Texture texture;
    private Texture texture2;
    private Circle mask;
    private Sector sector;

    private int size;
    private boolean selected;

    public Agent(
            final Vector2 position,
            final float velocity,
            final float angle,
            final Texture texture,
            final Texture texture2,
            final int size
    ) {
        this.position = position;
        this.velocity = velocity;
        this.angle = angle;
        this.texture = texture;
        this.texture2 = texture2;
        this.size = size;
        this.mask = new Circle(position.x, position.y, size);

        this.addListener(onclick());
    }

    public Circle getMask() {
        mask.setPosition(position);
        return mask;
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

    public Texture getTexture() {
        return selected ? texture2 : texture;
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        batch.draw(getTexture(), position.x, position.y);
    }

    private ClickListener onclick() {
        var agent = this;
        return new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                System.out.printf("%s clicked%n".formatted(agent.getId()));
                agent.setSelected(!agent.isSelected());
            }
        };
    }
}
