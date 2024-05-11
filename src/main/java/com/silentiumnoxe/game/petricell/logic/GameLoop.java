package com.silentiumnoxe.game.petricell.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.silentiumnoxe.game.petricell.model.Agent;
import com.silentiumnoxe.game.petricell.model.Sector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLoop {

    private static final float WORLD_WIDTH = Gdx.graphics.getWidth();
    private static final float WORLD_HEIGHT = Gdx.graphics.getHeight();
    public static final Circle WORLD_CIRCLE =
            new Circle((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 300);
    public static final int AGENT_COUNT = 1_000;

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

        for (int i = 0; i < AGENT_COUNT; i++) {
            var size = randomSize();

            var pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.RED);
            pixmap.drawCircle(pixmap.getWidth() / 2, pixmap.getHeight() / 2, size / 3);
            var texture = new Texture(pixmap);

            var x = new Agent(randomPosition(WORLD_CIRCLE), randomVelocity(), randomAngle(), texture,
                    new Rectangle(0, 0, 2, 2), null, randomSize());
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
                    Gdx.app.error("GameLoop", "Error in game loop - " + e.getMessage(), e);
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
                var agl = agent.getAngle();
                var rad = agl * Math.PI / 180;

                borderCollision(WORLD_CIRCLE, agent);
                agentCollision(agent, sector.getAgents());

                var oldX = pos.x;
                var oldY = pos.y;

                pos.set(
                        (float) (pos.x + vel * Math.cos(rad)),
                        (float) (pos.y + vel * Math.sin(rad))
                );
//                pos.set(
//                        pos.x - 1f,
//                        pos.y
//                );

                var sector2 = sector.findRelative(pos);
                if (sector2 != null && sector != sector2) {
                    sector.remove(agent);
                    sector2.add(agent);
                    agent.setSector(sector2);
                }
            }
        }
    }

    private void borderCollision(final Circle border, final Agent agent) {
        var pos = agent.getPosition();

        if (border.contains(pos.x, pos.y)) {
            return;
        }

        agent.setAngle(agent.getAngle() + 180);
    }

    private void agentCollision(final Agent agent, final Array<Agent> targets) {
//        for (var i = 0; i < targets.size; i++) {
//            var target = targets.get(i);
//
//            if (agent.getId() == target.getId()) {
//                continue;
//            }
//
//            if (agent.getSector().getId() != target.getSector().getId()) {
//                continue;
//            }
//
//            if (target.getMask().overlaps(agent.getMask())) {
//                target.getVelocity().x = -target.getVelocity().x;
//                target.getVelocity().y = -target.getVelocity().y;
//                agent.getVelocity().x = -agent.getVelocity().x;
//                agent.getVelocity().y = -agent.getVelocity().y;
//            }
//        }
    }

    private float randomVelocity() {
        var r = new Random();
        return r.nextFloat(1.0f);
    }

    private float randomAngle() {
        var r = new Random();
        return r.nextFloat(360f);
    }

    private Vector2 randomPosition(final Circle border) {
        var r = new Random();
        return new Vector2(
                r.nextFloat(border.x - border.radius / 2, border.x + border.radius / 2),
                r.nextFloat(border.y - border.radius / 2, border.y + border.radius / 2)
        );
    }

    private int randomSize() {
        var r = new Random();
        return r.nextInt(10, 20);
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
