package com.silentiumnoxe.game.petricell.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.silentiumnoxe.game.petricell.logic.GameLoop;

import java.text.DecimalFormat;

public class GameScreen extends BaseScreen {

    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font = new BitmapFont();
    private final GameLoop gameLoop = new GameLoop();
    private final DecimalFormat df = new DecimalFormat("#,###.##");

    public GameScreen() {

    }

    @Override
    public void show() {
        gameLoop.start();
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(Color.BLACK);

        var agents = gameLoop.getAgents();

        batch.begin();

        for (var i = 0; i < agents.size; i++) {
            var agent = agents.get(i);
            batch.draw(agent.getTexture(), agent.getPosition().x, agent.getPosition().y);
        }

        font.draw(batch, "FPS: %d".formatted(Gdx.graphics.getFramesPerSecond()), 0f, 60f);
        font.draw(batch, "UPS: %d".formatted(GameLoop.getUpdatesPerSecond()), 0f, 40f);
        font.draw(batch, "Agents: %s".formatted(df.format(agents.size)), 0f, 20f);
        batch.end();
    }
}
