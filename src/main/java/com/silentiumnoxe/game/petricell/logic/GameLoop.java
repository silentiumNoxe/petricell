package com.silentiumnoxe.game.petricell.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.silentiumnoxe.game.petricell.model.Agent;

import java.util.Random;

public class GameLoop {

    private static int updatesPerSecond = 0;

    private final Array<Agent> agents = new Array<>();

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
        var pixmap = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.drawCircle(10, 10, 5);
        var texture = new Texture(pixmap);

        for (int i = 0; i < 1_000; i++) {
            var x = new Agent(new Vector3(500f, 500f, 0), randomVelocity(), texture);
            agents.add(x);
        }
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                update(Gdx.graphics.getDeltaTime());

                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException ignore) {

                }
            }
        }).start();
    }

    private void update(final float delta) {
        countUPS();

        for (var i = 0; i < agents.size; i++) {
            var x = agents.get(i);

            var pos = x.getPosition().add(x.getVelocity());
            x.setVelocity(borderCollision(pos, x.getVelocity()));
            x.setPosition(pos);
        }
    }

    private Vector3 borderCollision(final Vector3 pos, final Vector3 velocity) {
        var v = velocity;

        if (pos.x < 0 || pos.x > Gdx.graphics.getWidth()) {
            v = new Vector3(-v.x, v.y, 0);
        }
        if (pos.y < 0 || pos.y > Gdx.graphics.getHeight()) {
            v = new Vector3(v.x, -v.y, 0);
        }

        return v;
    }

    private Vector3 randomVelocity() {
        var r = new Random();
        return new Vector3(r.nextFloat(-1.0f, 1.0f), r.nextFloat(-1.0f, 1.0f), 0);
    }

    void countUPS() {
        long time = System.nanoTime();
        if (lastFrameTime == -1) lastFrameTime = time;
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
}
