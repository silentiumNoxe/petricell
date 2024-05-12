package com.silentiumnoxe.game.petricell.logic.physic;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class PhysicProcessorAggregator implements PhysicProcessor {

    private final List<PhysicProcessor> list = new ArrayList<>();

    @Override
    public void process(final Actor[] arr, final int i, final Actor actor) {
        list.forEach(c -> c.process(arr, i, actor));
    }

    public void add(final PhysicProcessor processor) {
        list.add(processor);
    }
}
