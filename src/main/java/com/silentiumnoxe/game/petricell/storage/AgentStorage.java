package com.silentiumnoxe.game.petricell.storage;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.SnapshotArray;
import com.silentiumnoxe.game.petricell.model.Agent;

import java.util.UUID;

public class AgentStorage {

    private final Array<Agent> store = new Array<>();
    private final ArrayMap<String, Array<Agent>> sortedBySector = new ArrayMap<>();

    //todo: need to define storage for read only operations

    public void add(final Agent agent) {
        store.add(agent);

        var sectorId = agent.getSectorId().toString();
        if (!sortedBySector.containsKey(sectorId)) {
            var arr = new Array<Agent>();
            arr.add(agent);
            sortedBySector.put(sectorId, arr);
        } else {
            sortedBySector.get(sectorId).add(agent);
        }
    }

    public void remove(final Agent agent) {
        store.removeValue(agent, true);
    }

    public SnapshotArray<Agent> getAgentsInSector(final UUID sectorId) {
        var x = sortedBySector.get(sectorId.toString());
        if (x == null) {
            return new SnapshotArray<>(0);
        }
        return new SnapshotArray<>(x);
    }

    public SnapshotArray<Agent> snapshot() {
        return new SnapshotArray<>(store);
    }

    public int count() {
        return store.size;
    }
}
