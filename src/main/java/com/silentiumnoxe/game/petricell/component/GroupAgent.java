package com.silentiumnoxe.game.petricell.component;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.silentiumnoxe.game.petricell.model.Agent;
import lombok.Getter;

public class GroupAgent extends Group {

    @Getter
    private final Array<Agent> agents = new Array<>();

    public GroupAgent() {
        super();
        setName("gr-agents");
    }

    public void addAgent(final Agent agent) {
        agents.add(agent);
        addActor(agent);
    }

    public void removeAgent(final Agent agent) {
        agents.removeValue(agent, true);
        agent.remove();
    }
}
