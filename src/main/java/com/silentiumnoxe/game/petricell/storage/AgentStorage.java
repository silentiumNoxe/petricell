package com.silentiumnoxe.game.petricell.storage;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.silentiumnoxe.game.petricell.model.Agent;

public class AgentStorage {

    private final Array<Agent> store = new Array<>();

    //todo: need to define storage for read only operations

    public void add(final Agent agent) {
        store.add(agent);
    }

    public void remove(final Agent agent) {
        store.removeValue(agent, true);
    }

    public SnapshotArray<Agent> snapshot() {
        return new SnapshotArray<>(store);
    }

    public int count() {
        return store.size;
    }
}
