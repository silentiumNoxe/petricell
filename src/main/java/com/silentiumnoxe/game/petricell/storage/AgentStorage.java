package com.silentiumnoxe.game.petricell.storage;

import com.badlogic.gdx.utils.Array;
import com.silentiumnoxe.game.petricell.model.Agent;

public class AgentStorage {

    private final Array<Agent> agents = new Array<>();

    public void add(final Agent agent) {
        agents.add(agent);
    }

    public void remove(final Agent agent) {
        agents.removeValue(agent, true);
    }

    public void getAll() {
        return;
    }
}
