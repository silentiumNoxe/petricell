package com.silentiumnoxe.game.petricell.logic;

import com.badlogic.gdx.InputProcessor;

import java.util.LinkedList;
import java.util.List;

public class GlobalInputListener implements InputProcessor {

    private static GlobalInputListener instance;

    public static GlobalInputListener getInstance() {
        if (instance == null) {
            instance = new GlobalInputListener();
        }
        return instance;
    }

    private final List<InputProcessor> listeners = new LinkedList<>();

    public void addListener(final InputProcessor listener) {
        listeners.add(listener);
    }

    @Override
    public boolean keyDown(final int i) {
        for (var listener : listeners) {
            listener.keyDown(i);
        }
        return false;
    }

    @Override
    public boolean keyUp(final int i) {
        for (var listener : listeners) {
            listener.keyUp(i);
        }
        return false;
    }

    @Override
    public boolean keyTyped(final char c) {
        for (var listener : listeners) {
            listener.keyTyped(c);
        }
        return false;
    }

    @Override
    public boolean touchDown(final int i, final int i1, final int i2, final int i3) {
        for (var listener : listeners) {
            listener.touchDown(i, i1, i2, i3);
        }
        return false;
    }

    @Override
    public boolean touchUp(final int i, final int i1, final int i2, final int i3) {
        for (var listener : listeners) {
            listener.touchUp(i, i1, i2, i3);
        }
        return false;
    }

    @Override
    public boolean touchCancelled(final int i, final int i1, final int i2, final int i3) {
        for (var listener : listeners) {
            listener.touchCancelled(i, i1, i2, i3);
        }
        return false;
    }

    @Override
    public boolean touchDragged(final int i, final int i1, final int i2) {
        for (var listener : listeners) {
            listener.touchDragged(i, i1, i2);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(final int i, final int i1) {
        for (var listener : listeners) {
            listener.mouseMoved(i, i1);
        }
        return false;
    }

    @Override
    public boolean scrolled(final float v, final float v1) {
        for (var listener : listeners) {
            listener.scrolled(v, v1);
        }
        return false;
    }
}
