package com.silentiumnoxe.game.petricell.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Setter
@Getter
public class Sector extends Rectangle {

    private final Texture texture;
    private final long id = new Random().nextLong();
    private final Array<Agent> agents = new Array<>();
    private final boolean border;

    private Sector left;
    private Sector right;
    private Sector top;
    private Sector bottom;

    public Sector(final float x, final float y, final float width, final float height, final boolean border) {
        super(x, y, width, height);
        this.border = border;

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
        agents.removeValue(agent, true);
    }

    public void remove(final int i) {
        agents.removeIndex(i);
    }

    public int getSize() {
        return agents.size;
    }

    public Sector findRelative(final Vector2 pos) {
        if (pos.x < x) {
            return left;
        } else if (pos.x > x + width) {
            return right;
        } else if (pos.y < y) {
            return bottom;
        } else if (pos.y > y + height) {
            return top;
        }
        return this;
    }
}
