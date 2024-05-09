package com.silentiumnoxe.game.petricell.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.silentiumnoxe.game.petricell.model.Agent;
import com.silentiumnoxe.game.petricell.model.Sector;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLoop {

    private static final float WORLD_WIDTH = Gdx.graphics.getWidth();
    private static final float WORLD_HEIGHT = Gdx.graphics.getHeight();
    private static final int agentCount = 100_000;

    private static int updatesPerSecond = 0;

    private final Array<Agent> agents = new Array<>();
    private final List<Sector> sectors = new ArrayList<>();

    private long lastFrameTime = -1;
    private float deltaTime;
    private boolean resetDeltaTime = false;
    private long frameId;
    private long frameCounterStart = 0;
    private int frames;

    public static int getUpdatesPerSecond() {
        return updatesPerSecond;
    }

    public GameLoop() {
        splitScreen(70 * 70, WORLD_WIDTH, WORLD_HEIGHT);

        var pixmap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillCircle(2, 2, 1);
        var texture = new Texture(pixmap);

        for (int i = 0; i < agentCount; i++) {
            var x = new Agent(randomPosition(), randomVelocity(), texture, new Rectangle(0, 0, 2, 2), null);
            for (Sector s : sectors) {
                if (s.overlaps(x.getMask())) {
                    s.add(x);
                    x.setSector(s);
                }
            }
            agents.add(x);
        }
    }

    private void splitScreen(final int parts, final float width, final float height) {
        var side = (float) Math.sqrt(parts);
        var sectorWidth = width / side;
        var sectorHeight = height / side;

        int si = -1;
        for (var i = 0; i < side; i++) {
            for (var k = 0; k < side; k++) {
                var x = i * sectorWidth;
                var y = k * sectorHeight;
                var isBorder = x == 0 || y == 0 || x == width - sectorWidth || y == height - sectorHeight;
                var sector = new Sector(x, y, sectorWidth, sectorHeight, isBorder);
                sectors.add(sector);
                si++;

                var left = si - side;
                var bottom = si - 1;

                if (left >= 0) {
                    sector.setLeft(sectors.get((int) left));
                    sectors.get((int) left).setRight(sector);
                }

                if (bottom >= 0) {
                    sector.setBottom(sectors.get(bottom));
                    sectors.get(bottom).setTop(sector);
                }
            }
        }
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    update();
                } catch (Exception e) {
                    Gdx.app.error("GameLoop", "Error in game loop - "+e.getMessage(), e);
                }

                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException ignore) {

                }
            }
        }).start();
    }

    private void update() {
        countUPS();

        for (var i = 0; i < sectors.size(); i++) {
            var sector = sectors.get(i);
            for (var j = 0; j < agents.size; j++) {
                if (j >= sector.getAgents().size) {
                    break;
                }
                var agent = sector.getAgents().get(j);
                var pos = agent.getPosition();
                var vel = agent.getVelocity();

                if (sector.isBorder()) {
                    borderCollision(pos, vel);
                }

                agentCollision(agent, sector.getAgents());
                pos.add(vel);

                var sector2 = sector.findRelative(pos);
                if (sector2 != null && sector != sector2) {
                    sector.remove(agent);
                    sector2.add(agent);
                    agent.setSector(sector2);
                }
            }
        }
    }

    private void agentCollision(final Agent agent, final Array<Agent> targets) {
        for (var i = 0; i < targets.size; i++) {
            var target = targets.get(i);

            if (agent.getId() == target.getId()) {
                continue;
            }

            if (agent.getSector().getId() != target.getSector().getId()) {
                continue;
            }

            if (target.getMask().overlaps(agent.getMask())) {
                target.getVelocity().x = -target.getVelocity().x;
                target.getVelocity().y = -target.getVelocity().y;
                agent.getVelocity().x = -agent.getVelocity().x;
                agent.getVelocity().y = -agent.getVelocity().y;
            }
        }
    }

    private void borderCollision(final Vector3 pos, final Vector3 velocity) {
        if (pos.x < 0 || pos.x > WORLD_WIDTH) {
            velocity.x = -velocity.x;
        }
        if (pos.y < 0 || pos.y > WORLD_HEIGHT) {
            velocity.y = -velocity.y;
        }
    }

    private Vector3 randomVelocity() {
        var r = new Random();
        return new Vector3(r.nextFloat(-2.0f, 2.0f), r.nextFloat(-2.0f, 2.0f), 0);
    }

    private Vector3 randomPosition() {
        var r = new Random();
        return new Vector3(r.nextFloat(0, WORLD_WIDTH), r.nextFloat(0, WORLD_HEIGHT), 0);
    }

    void countUPS() {
        long time = System.nanoTime();
        if (lastFrameTime == -1)
            lastFrameTime = time;
        if (resetDeltaTime) {
            resetDeltaTime = false;
            deltaTime = 0;
        } else
            deltaTime = (time - lastFrameTime) / 1000000000.0f;
        lastFrameTime = time;

        if (time - frameCounterStart >= 1000000000) {
            updatesPerSecond = frames;
            frames = 0;
            frameCounterStart = time;
        }
        frames++;
        frameId++;
    }

    public Array<Agent> getAgents() {
        return agents;
    }

    public List<Sector> getSectors() {
        return sectors;
    }
}
