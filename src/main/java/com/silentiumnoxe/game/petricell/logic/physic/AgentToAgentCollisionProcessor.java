package com.silentiumnoxe.game.petricell.logic.physic;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.silentiumnoxe.game.petricell.model.Agent;

public class AgentToAgentCollisionProcessor implements PhysicProcessor {

    @Override
    public void process(final Actor[] arr, final int i, final Actor actor) {
//        if (actor instanceof Agent agent) {
//            calc(arr, i, agent);
//        }
    }

    private void calc(final Actor[] arr, final int i, final Agent a) {
        for (var actor : arr) {
            if (actor instanceof Agent b) {
                if (a == b) {
                    continue;
                }

                calc(a, b);
            }
        }
    }

    private void calc(final Agent a, final Agent b) {
        float dx = a.getX() - b.getX();
        float dy = a.getY() - b.getY();
        float distance = dx * dx + dy * dy;
        int radiusSum = a.getSize() + b.getSize();
        boolean overlaps = distance < radiusSum + radiusSum;

        if (overlaps) {
            var sum = a.getAngle() + b.getAngle();
            a.setVelocity(-a.getVelocity());
            a.setAngle(-a.getAngle());
        }
    }
}
