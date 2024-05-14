package com.silentiumnoxe.game.petricell.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.silentiumnoxe.game.petricell.storage.AgentStorage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AgentRenderer implements Renderer {

    private final AgentStorage agentStorage;

    @Override
    public void render(final Batch batch, final float delta) {
        var snap = agentStorage.snapshot();
        snap.begin();
        for (var i = 0; i < snap.size; i++) {
            var agent = snap.get(i);
            agent.draw(batch, 1f);
        }
        snap.end();
    }
}
