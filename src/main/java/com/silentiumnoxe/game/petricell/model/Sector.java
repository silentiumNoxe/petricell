package com.silentiumnoxe.game.petricell.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class Sector extends Rectangle {

    private final Texture texture;
    private final UUID id = UUID.randomUUID();
    private final List<Agent> agents = new LinkedList<>();

    public Sector(final float x, final float y, final float width, final float height) {
        super(x, y, width, height);

        Pixmap pixmap = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.drawRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        texture = new Texture(pixmap);
    }

    public void draw(final SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public void add(final Agent agent) {
        agents.add(agent);
    }

    public void remove(final Agent agent) {
        agents.remove(agent);
    }

    public int getSize() {
        return agents.size();
    }
}
