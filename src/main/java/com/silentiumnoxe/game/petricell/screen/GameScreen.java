package com.silentiumnoxe.game.petricell.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.silentiumnoxe.game.petricell.component.GroupAgent;
import com.silentiumnoxe.game.petricell.component.KVLabel;
import com.silentiumnoxe.game.petricell.logic.GameLoop;
import com.silentiumnoxe.game.petricell.logic.SelectedAgentHolder;
import com.silentiumnoxe.game.petricell.logic.ZoomValueHolder;
import com.silentiumnoxe.game.petricell.util.SizeFormatter;

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
        agentPos.setPosition(selectedAgentStatsGroup.getX(), selectedAgentStatsGroup.getY() + selectedAgentStatsGroup.getHeight());
        selectedAgentStatsGroup.addActor(agentPos);

        var agentVel = new KVLabel("Velocity", defaultFont);
        agentVel.setName("stat-agent-vel");
        agentVel.setPosition(selectedAgentStatsGroup.getX(), agentPos.getY() - margin);
        selectedAgentStatsGroup.addActor(agentVel);

        var agentAngle = new KVLabel("Angle", defaultFont);
        agentAngle.setName("stat-agent-angle");
        agentAngle.setPosition(selectedAgentStatsGroup.getX(), agentVel.getY() - margin);
        selectedAgentStatsGroup.addActor(agentAngle);

        var agentSize = new KVLabel("Size", defaultFont);
        agentSize.setName("stat-agent-size");
        agentSize.setPosition(selectedAgentStatsGroup.getX(), agentAngle.getY() - margin);
        selectedAgentStatsGroup.addActor(agentSize);

        var topGroup = new Group();
        topGroup.setName("gr-top");
        topGroup.setPosition((float) Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 25);
        stage.addActor(topGroup);

        var zoomVal = new KVLabel("Zoom", defaultFont);
        zoomVal.setName("zoom");
        topGroup.addActor(zoomVal);

        stage.addListener(new InputListener() {
            @Override
            public boolean scrolled(final InputEvent event, final float x, final float y, final float amountX,
                                    final float amountY) {
                var holder = ZoomValueHolder.getInstance();
                if (holder.getZoomMicro() < 100) {
                    holder.setZoom(holder.getZoom() + ((long) amountY) * 100);
                    return true;
                }
                if (holder.getZoomMilli() < 2) {
                    holder.setZoom(holder.getZoom() + ((long) amountY) * 10000);
                    return true;
                }
                holder.setZoom(holder.getZoom() + ((long) amountY) * 40000);
                return true;
            }
        });
    }

    @Override
    public void postRender(final float delta) {
        updateStats();
        updateSelectedAgentStats();
        updateZoom();
    }

    private void updateStats() {
        var root = stage.getRoot();
        ((KVLabel) root.findActor("stat-fps")).setValue(Gdx.graphics.getFramesPerSecond());
        ((KVLabel) root.findActor("stat-ups")).setValue(GameLoop.getUpdatesPerSecond());
        ((KVLabel) root.findActor("stat-agents")).setValue(df.format(GameLoop.AGENT_COUNT));
        ((KVLabel) root.findActor("stat-heap")).setValue(
                "%sMb".formatted(df.format(Gdx.app.getNativeHeap() / 1024 / 1024)));
    }

    private void updateSelectedAgentStats() {
        var agent = SelectedAgentHolder.getInstance().getSelected();
        if (agent == null) {
            return;
        }

        var root = stage.getRoot();
        ((KVLabel) root.findActor("stat-agent-pos")).setValue("%d:%d".formatted((int) agent.getX(), (int) agent.getY()));
        ((KVLabel) root.findActor("stat-agent-vel")).setValue(df.format(agent.getVelocity()));
        ((KVLabel) root.findActor("stat-agent-angle")).setValue(df.format(agent.getAngle()));
        ((KVLabel) root.findActor("stat-agent-size")).setValue(SizeFormatter.applyStatic(agent.getSize()));
    }

    private void updateZoom() {
        var root = stage.getRoot();
        var value = ZoomValueHolder.getInstance().getZoomNano();
        ((KVLabel) root.findActor("zoom")).setValue(SizeFormatter.applyStatic(value));
    }
}
