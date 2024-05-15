package com.silentiumnoxe.game.petricell.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.silentiumnoxe.game.petricell.logic.SelectedAgentHolder;
import lombok.AllArgsConstructor;
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

    private Circle mask;

    private int size;
    private boolean selected;

    private AgentStyle style;

    public Agent(
            final Vector2 position,
            final float velocity,
            final float angle,
            final int size,
            final AgentStyle style
    ) {
        this.setBounds(position.x, position.y, size, size);
        this.setWidth(size);
        this.setHeight(size);
        this.setPosition(position.x, position.y);

        this.velocity = velocity;
        this.angle = angle;
        this.size = size;
        this.mask = new Circle(position.x, position.y, size);

        this.style = style;

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
        return selected ? style.getSelection() : style.getPrimary();
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        batch.draw(getTexture(), getX(), getY(), getWidth(), getHeight());
    }

    public void draw(final Pixmap pixmap) {
        pixmap.setColor(Color.RED);
        pixmap.drawCircle((int) getX(), (int) getY(), size / 2);
    }

    private void onclick(final float x, final float y) {
        selected = true;
        SelectedAgentHolder.getInstance().setSelected(this);
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    @Getter
    public static class AgentStyle {
        private static final int SIZE = 25;

        private Color color1;
        private Color color2;

        private Texture primary;
        private Texture selection;

        public AgentStyle() {
            this(Color.RED, Color.BLACK);
        }

        public AgentStyle(final Color color1, final Color color2) {
            this.color1 = color1;
            this.color2 = color2;

            primary = generatePrimaryTexture(color1, color2);
            selection = generateSelectionTexture();
        }

        public void setColor1(final Color color1) {
            this.color1 = color1;
            primary = generatePrimaryTexture(color1, color2);
        }

        public void setColor2(final Color color2) {
            this.color2 = color2;
            primary = generatePrimaryTexture(color1, color2);
        }

        private Texture generatePrimaryTexture(final Color color1, final Color color2) {
            var pixmap = new Pixmap(SIZE, SIZE, Pixmap.Format.RGBA8888);
            pixmap.setColor(color1);
            pixmap.fillCircle(pixmap.getWidth() / 2, pixmap.getHeight() / 2, SIZE / 2);
            pixmap.setColor(color2);
            pixmap.fillCircle(pixmap.getWidth() / 2, pixmap.getHeight() / 2, SIZE / 4);
            return new Texture(pixmap);
        }

        private Texture generateSelectionTexture() {
            var pixmap = new Pixmap(SIZE, SIZE, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.drawCircle(pixmap.getWidth() / 2, pixmap.getHeight() / 2, SIZE / 2);
            return new Texture(pixmap);
        }
    }
}
