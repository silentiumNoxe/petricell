package com.silentiumnoxe.game.petricell.screen;

import com.badlogic.gdx.Game;
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
    private final Texture petriTexture;
    private final Texture pointTexture;

    public GameScreen() {
        var size = (int) GameLoop.WORLD_CIRCLE.radius+12;
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

//        var worldCircle = GameLoop.WORLD_CIRCLE;

//        batch.draw(petriTexture, worldCircle.x-worldCircle.radius-12, worldCircle.y-worldCircle.radius-12);
//        batch.draw(pointTexture, GameLoop.WORLD_CIRCLE.x, GameLoop.WORLD_CIRCLE.y);

        font.draw(batch, "FPS: %d".formatted(Gdx.graphics.getFramesPerSecond()), 10f, Gdx.graphics.getHeight() - 10f);
        font.draw(batch, "UPS: %d".formatted(GameLoop.getUpdatesPerSecond()), 10f, Gdx.graphics.getHeight() - 30f);
        font.draw(batch, "Agents: %s".formatted(df.format(GameLoop.AGENT_COUNT)), 10f, Gdx.graphics.getHeight() - 50f);
        font.draw(batch, "Heap: %sMb".formatted(df.format(Gdx.app.getNativeHeap() / 1024 / 1024)), 10f, Gdx.graphics.getHeight() - 90f);
        batch.end();
    }
}
