package com.silentiumnoxe.game.petricell.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.silentiumnoxe.game.petricell.component.KVLabel;
import com.silentiumnoxe.game.petricell.config.WorldConfig;
import com.silentiumnoxe.game.petricell.logic.GameLoop;
import com.silentiumnoxe.game.petricell.logic.SelectedAgentHolder;
import com.silentiumnoxe.game.petricell.render.AgentRenderer;
import com.silentiumnoxe.game.petricell.render.Renderer;
import com.silentiumnoxe.game.petricell.storage.AgentStorage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends BaseScreen {

    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final Stage stage;

    private final BitmapFont defaultFont = new BitmapFont();
    private final DecimalFormat df = new DecimalFormat("#,###.##");
    private final Texture petriTexture;
    private final Texture pointTexture;

    private final WorldConfig worldConfig;
    private final AgentStorage agentStorage;

    private final List<Renderer> rendererList = new ArrayList<>();

    private GameLoop gameLoop;

    public GameScreen() {
        worldConfig = new WorldConfig(300, (float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);

        stage = new Stage(new ScalingViewport(Scaling.stretch,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                new OrthographicCamera()
        ), batch);
        agentStorage = new AgentStorage();

        rendererList.add(new AgentRenderer(agentStorage));

        var size = worldConfig.getRadius() + 12;
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
        gameLoop = new GameLoop(worldConfig, agentStorage);
        gameLoop.start();

        var statsGroup = new Group();
        statsGroup.setName("gr-stats");
        statsGroup.setHeight(80);
        statsGroup.setPosition(0, 0);
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

        var selectedAgentStatsGroup = new Group();
        selectedAgentStatsGroup.setName("gr-selected-agent-stats");
        selectedAgentStatsGroup.setHeight(80);
        selectedAgentStatsGroup.setPosition(0, 300);
        stage.addActor(selectedAgentStatsGroup);

        var agentPos = new KVLabel("Position", defaultFont);
        agentPos.setName("stat-agent-pos");
        agentPos.setPosition(
                selectedAgentStatsGroup.getX(),
                selectedAgentStatsGroup.getY() + selectedAgentStatsGroup.getHeight()
        );
        selectedAgentStatsGroup.addActor(agentPos);

        var agentVel = new KVLabel("Velocity", defaultFont);
        agentVel.setName("stat-agent-vel");
        agentVel.setPosition(selectedAgentStatsGroup.getX(), agentPos.getY() - margin);
        selectedAgentStatsGroup.addActor(agentVel);

        var agentAngle = new KVLabel("Angle", defaultFont);
        agentAngle.setName("stat-agent-angle");
        agentAngle.setPosition(selectedAgentStatsGroup.getX(), agentVel.getY() - margin);
        selectedAgentStatsGroup.addActor(agentAngle);
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(Color.BLACK);

        batch.begin();
        var snapshot = gameLoop.getSnapshot();
        if (snapshot != null) {
            batch.draw(new Texture(snapshot), 0, 0);
        }
        batch.end();

        stage.act();
        stage.draw();

        updateStats();
        updateSelectedAgentStats();
    }

    private void updateStats() {
        var root = stage.getRoot();
        ((KVLabel) root.findActor("stat-fps")).setValue(Gdx.graphics.getFramesPerSecond());
        ((KVLabel) root.findActor("stat-ups")).setValue(GameLoop.getUpdatesPerSecond());
        ((KVLabel) root.findActor("stat-agents")).setValue(df.format(agentStorage.count()));
        ((KVLabel) root.findActor("stat-heap")).setValue(
                "%sMb".formatted(df.format(Gdx.app.getNativeHeap() / 1024 / 1024)));
    }

    private void updateSelectedAgentStats() {
        var agent = SelectedAgentHolder.getInstance().getSelected();
        if (agent == null) {
            return;
        }

        var root = stage.getRoot();
        ((KVLabel) root.findActor("stat-agent-pos")).setValue("%d:%d".formatted(
                (int) agent.getX(),
                (int) agent.getY()
        ));
        ((KVLabel) root.findActor("stat-agent-vel")).setValue(df.format(agent.getVelocity()));
        ((KVLabel) root.findActor("stat-agent-angle")).setValue(df.format(agent.getAngle()));
    }
}
