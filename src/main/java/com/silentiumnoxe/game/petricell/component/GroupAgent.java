package com.silentiumnoxe.game.petricell.component;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.silentiumnoxe.game.petricell.model.Agent;

public class GroupAgent extends Group {

    public GroupAgent() {
        super();
        setName("gr-agents");
    }

    public void add(final Agent agent) {
        addActor(agent);
    }

    public void remove(final Agent agent) {
        agent.remove();
    }
}
