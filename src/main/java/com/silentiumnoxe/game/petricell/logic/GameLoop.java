package com.silentiumnoxe.game.petricell.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.silentiumnoxe.game.petricell.config.WorldConfig;
import com.silentiumnoxe.game.petricell.model.Agent;
import com.silentiumnoxe.game.petricell.model.Sector;
import com.silentiumnoxe.game.petricell.storage.AgentStorage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLoop {

    public static final int AGENT_COUNT = 70_000;

    private static int updatesPerSecond = 0;

    private final WorldConfig worldConfig;
    private final AgentStorage agentStorage;

    private final Array<Agent> agents = new Array<>();
    private final List<Sector> sectors = new ArrayList<>();

    private Pixmap snapshot;

    private boolean started;

    private long lastFrameTime = -1;
    private float deltaTime;
    private boolean resetDeltaTime = false;
    private long frameId;
    private long frameCounterStart = 0;
    private int frames;

    public static int getUpdatesPerSecond() {
        return updatesPerSecond;
    }

    public GameLoop(final WorldConfig worldConfig, final AgentStorage agentStorage) {
        this.worldConfig = worldConfig;
        this.agentStorage = agentStorage;

        splitScreen(5 * 5, worldConfig.getWidth(), worldConfig.getHeight());

        for (int i = 0; i < AGENT_COUNT; i++) {
            var x = new Agent(
                    randomPosition(worldConfig.getShape()),
                    randomVelocity(),
                    randomAngle(),
                    randomSize(),
                    new Agent.AgentStyle()
            );
            for (Sector s : sectors) {
                if (s.contains(x.getMask())) {
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
        if (started) {
            return;
        }

        started = true;

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

        var pixmap = new Pixmap(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Pixmap.Format.RGBA8888);

        for (var j = 0; j < agents.size; j++) {

            var agent = agents.get(j);
            var pos = agent.getPosition();
            var vel = agent.getVelocity();
            var agl = agent.getAngle();
            var rad = agl * Math.PI / 180;

            borderCollision(worldConfig.getShape(), agent);
            agentCollision(agent);

            agent.setPosition(
                    (float) (pos.x + vel * Math.cos(rad)),
                    (float) (pos.y + vel * Math.sin(rad))
            );

            agent.draw(pixmap);
        }

        snapshot = pixmap;
    }

    private void borderCollision(final Circle border, final Agent agent) {
        var pos = agent.getPosition();

        if (border.contains(pos.x, pos.y)) {
            return;
        }

        agent.setVelocity(0);
    }

    private void agentCollision(final Agent agent) {
//        var sector = agent.getSector();
//        var targets = sector.getAgents();
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
//            if (agent.getMask().overlaps(target.getMask())) {
//                agent.setVelocity(-agent.getVelocity());
//                target.setVelocity(-target.getVelocity());
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
//        return 90;
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
        return r.nextInt(6, 16);
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

    public List<Sector> getSectors() {
        return sectors;
    }

    public Pixmap getSnapshot() {
        var x = snapshot;
        snapshot = null;
        return x;
    }
}
