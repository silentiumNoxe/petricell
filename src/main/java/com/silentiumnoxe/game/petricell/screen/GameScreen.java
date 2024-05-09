package com.silentiumnoxe.game.petricell.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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

        var sectors = gameLoop.getSectors();
//        for (var sector : sectors) {
//            sector.draw(batch);
//
//            font.draw(
//                    batch,
//                    "%d".formatted(sector.getSize()),
//                    sector.getX() + 10,
//                    sector.getY() + 20
//            );
//        }

        font.draw(batch, "FPS: %d".formatted(Gdx.graphics.getFramesPerSecond()), 10f, Gdx.graphics.getHeight() - 10f);
        font.draw(batch, "UPS: %d".formatted(GameLoop.getUpdatesPerSecond()), 10f, Gdx.graphics.getHeight() - 30f);
        font.draw(batch, "Agents: %s".formatted(df.format(agents.size)), 10f, Gdx.graphics.getHeight() - 50f);
        font.draw(batch, "Sectors: %s".formatted(df.format(sectors.size())), 10f, Gdx.graphics.getHeight() - 70f);
        font.draw(batch, "Heap: %sMb".formatted(df.format(Gdx.app.getNativeHeap() / 1024 / 1024)), 10f, Gdx.graphics.getHeight() - 90f);
        batch.end();
    }
}
