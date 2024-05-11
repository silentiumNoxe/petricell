package com.silentiumnoxe.game.petricell.logic;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.silentiumnoxe.game.petricell.model.Agent;
import com.silentiumnoxe.game.petricell.model.Sector;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public class GameLoopControl implements InputProcessor {

    private final Array<Agent> agents;
    private Agent selectedAgent = null;

    @Override
    public boolean keyDown(final int i) {
        return false;
    }

    @Override
    public boolean keyUp(final int i) {
        return false;
    }

    @Override
    public boolean keyTyped(final char c) {
        return false;
    }

    @Override
    public boolean touchDown(final int x, final int y, final int i2, final int i3) {
        System.out.printf("touchDown: x:%d, y:%d%n", x, y);
        for (var i = 0; i < agents.size; i++) {
            var agent = agents.get(i);
            if (agent.getMask().contains(x, y)) {
                System.out.printf("selected agent pos x:%f y:%f%n", agent.getPosition().x, agent.getPosition().y);
                if (selectedAgent != null) {
                    selectedAgent.setSelected(false);
                }
                agent.setSelected(true);
                selectedAgent = agent;
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(final int i, final int i1, final int i2, final int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(final int i, final int i1, final int i2, final int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(final int i, final int i1, final int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(final int i, final int i1) {
        return false;
    }

    @Override
    public boolean scrolled(final float v, final float v1) {
        return false;
    }
}
