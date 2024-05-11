package com.silentiumnoxe.game.petricell.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.silentiumnoxe.game.petricell.component.GroupAgent;
import com.silentiumnoxe.game.petricell.component.KVLabel;
import com.silentiumnoxe.game.petricell.logic.GameLoop;
import com.silentiumnoxe.game.petricell.logic.GlobalInputListener;
import com.silentiumnoxe.game.petricell.model.Agent;
import org.checkerframework.checker.units.qual.A;

import java.text.DecimalFormat;

public class GameScreen extends BaseScreen {

    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont defaultFont = new BitmapFont();
    private final DecimalFormat df = new DecimalFormat("#,###.##");
    private final Texture petriTexture;
    private final Texture pointTexture;

    private GameLoop gameLoop;

    public GameScreen() {
        super();

        var size = (int) GameLoop.WORLD_CIRCLE.radius + 12;
        Pixmap pixmap = new Pixmap(size * 2 + 10, size * 2 + 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.drawCircle(pixmap.getWidth() / 2, pixmap.getHeight() / 2, size);
        petriTexture = new Texture(pixmap);

        Pixmap pixmap2 = new Pixmap(6, 6, Pixmap.Format.RGBA8888);
        pixmap2.setColor(Color.GREEN);
        pixmap2.fillCircle(pixmap2.getWidth() / 2, pixmap2.getHeight() / 2, 2);
        pointTexture = new Texture(pixmap2);
    }

    @Override
    public void show() {
        super.show();

        var groupAgents = new GroupAgent();
        stage.addActor(groupAgents);
        gameLoop = new GameLoop(groupAgents);
        gameLoop.start();

        var statsGroup = new Group();
        statsGroup.setName("gr-stats");
        statsGroup.setHeight(80);
        stage.addActor(statsGroup);

        var margin = 25;

        var fps = new KVLabel("FPS", defaultFont);
        fps.setName("stat-fps");
        fps.setPosition(statsGroup.getX(), statsGroup.getY() + statsGroup.getHeight());
        statsGroup.addActor(fps);

        var ups = new KVLabel("UPS", defaultFont);
        ups.setName("stat-ups");
        ups.setPosition(statsGroup.getX(), fps.getY() - margin);
        statsGroup.addActor(ups);

        var agents = new KVLabel("Agents", defaultFont);
        agents.setName("stat-agents");
        agents.setPosition(statsGroup.getX(), ups.getY() - margin);
        statsGroup.addActor(agents);

        var heap = new KVLabel("Heap", defaultFont);
        heap.setName("stat-heap");
        heap.setPosition(statsGroup.getX(), agents.getY() - margin);
        statsGroup.addActor(heap);
    }

    @Override
    public void postRender(final float delta) {
        updateStats();
    }

    private void updateStats() {
        var root = stage.getRoot();
        ((KVLabel) root.findActor("stat-fps")).setValue(Gdx.graphics.getFramesPerSecond());
        ((KVLabel) root.findActor("stat-ups")).setValue(GameLoop.getUpdatesPerSecond());
        ((KVLabel) root.findActor("stat-agents")).setValue(df.format(GameLoop.AGENT_COUNT));
        ((KVLabel) root.findActor("stat-heap")).setValue(
                "%sMb".formatted(df.format(Gdx.app.getNativeHeap() / 1024 / 1024)));
    }
}
