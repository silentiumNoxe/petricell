package com.silentiumnoxe.game.petricell.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.silentiumnoxe.game.petricell.logic.SelectedAgentHolder;
import com.silentiumnoxe.game.petricell.logic.ZoomValueHolder;
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
    private float velocity;
    private float angle;
    private Texture texture;
    private Texture texture2;
    private Circle mask;
    private Sector sector;

    // value in nanometers
    private long size;
    private boolean selected;

    public Agent(
            final Vector2 position,
            final float velocity,
            final float angle,
            final Texture texture,
            final Texture texture2,
            final long size
    ) {
        this.setBounds(position.x, position.y, size, size);
        this.setWidth(size);
        this.setHeight(size);
        this.setPosition(position.x, position.y);

        this.velocity = velocity;
        this.angle = angle;
        this.texture = texture;
        this.texture2 = texture2;
        this.size = size;
        this.mask = new Circle(position.x, position.y, size);

        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                onclick(x, y);
            }
        });
    }

    public Circle getMask() {
        mask.setPosition(getX(), getY());
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
        var zoom = ZoomValueHolder.getInstance().getZoomNano();
        var size = this.size / zoom;
        if (size > 5000) {
            return;
        }
        batch.draw(getTexture(), getX(), getY(), size, size);
    }

    private void onclick(final float x, final float y) {
        selected = true;
        SelectedAgentHolder.getInstance().setSelected(this);
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }
}
