package com.silentiumnoxe.game.petricell.logic.physic;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.silentiumnoxe.game.petricell.logic.GameLoop;
import com.silentiumnoxe.game.petricell.model.Agent;

public class BorderCollisionProcessor implements PhysicProcessor {

    @Override
    public void process(final Actor[] arr, final int i, final Actor actor) {
        if (actor instanceof Agent agent) {
            calc(agent);
        }
    }

    private void calc(final Agent agent) {
        var x = agent.getX();
        var y = agent.getY();
        var radius = agent.getSize();
        var angle = agent.getAngle();

        var x2 = GameLoop.WORLD_CIRCLE.x;
        var y2 = GameLoop.WORLD_CIRCLE.y;
        var radius2 = GameLoop.WORLD_CIRCLE.radius;

        var dist = Math.hypot(x - x2, y - y2);
        if (dist + radius < radius2) {
            return;
        }

        agent.setAngle(angle * 2);
    }
}
