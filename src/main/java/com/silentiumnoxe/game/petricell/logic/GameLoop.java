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
import java.util.UUID;

public class GameLoop {

    private static final float WORLD_WIDTH = Gdx.graphics.getWidth() * 2;
    private static final float WORLD_HEIGHT = Gdx.graphics.getHeight() * 2;

    private static int updatesPerSecond = 0;

    private final Array<Agent> agents = new Array<>();
    private final List<Sector> sectors = new ArrayList<>();
    @Getter
    private final UUID sectorId;

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
        splitScreen(64 * 64, WORLD_WIDTH, WORLD_HEIGHT);
        sectorId = sectors.get(sectors.size() / 3).getId();

        var pixmap = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillCircle(10, 10, 1);
        var texture = new Texture(pixmap);

        for (int i = 0; i < 100_000; i++) {
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

        for (var i = 0; i < side; i++) {
            for (var k = 0; k < side; k++) {
                var x = i * sectorWidth;
                var y = k * sectorHeight;
                sectors.add(new Sector(x, y, sectorWidth, sectorHeight));
            }
        }
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                update();

                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException ignore) {

                }
            }
        }).start();
    }

    private void update() {
        countUPS();

        for (var i = 0; i < agents.size; i++) {
            var agent = agents.get(i);
            var agentSector = agent.getSector();

            var pos = agent.getPosition();
            agent.setVelocity(borderCollision(pos, agent.getVelocity()));
            agent.setVelocity(agentCollision(agent, agentSector.getAgents()));
            pos = pos.add(agent.getVelocity());
            agent.setPosition(pos);

            if (!agentSector.overlaps(agent.getMask())) {
                for (var s : sectors) {
                    if (s.getId() == agentSector.getId()) {
                        continue;
                    }

                    if (s.overlaps(agent.getMask())) {
                        agentSector.remove(agent);
                        s.add(agent);
                        agent.setSector(s);
                    }
                }
            }
        }
    }

    private Vector3 agentCollision(final Agent agent, final List<Agent> targets) {
        var pos = agent.getPosition();
        agent.getMask().setPosition(pos.x, pos.y);
        for (var target : targets) {
            if (agent.getId() == target.getId()) {
                continue;
            }

            target.getMask().setPosition(target.getPosition().x, target.getPosition().y);
            if (target.getMask().overlaps(agent.getMask())) {
                target.setVelocity(new Vector3(-target.getVelocity().x, -target.getVelocity().y, 0));
                return new Vector3(-agent.getVelocity().x, -agent.getVelocity().y, 0);
            }
        }

        return agent.getVelocity();
    }

    private Vector3 borderCollision(final Vector3 pos, final Vector3 velocity) {
        var v = velocity;

        if (pos.x < 0 || pos.x > WORLD_WIDTH) {
            v = new Vector3(-v.x, v.y, 0);
        }
        if (pos.y < 0 || pos.y > WORLD_HEIGHT) {
            v = new Vector3(v.x, -v.y, 0);
        }

        return v;
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
