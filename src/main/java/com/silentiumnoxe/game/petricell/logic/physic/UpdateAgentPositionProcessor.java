package com.silentiumnoxe.game.petricell.logic.physic;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.silentiumnoxe.game.petricell.model.Moveable;

public class UpdateAgentPositionProcessor implements PhysicProcessor {

    @Override
    public void process(final Actor[] arr, final int i, final Actor actor) {
        if (actor instanceof Moveable m) {
            var x = actor.getX();
            var y = actor.getY();
            var rad = m.getAngleRad();
            var vel = m.getVelocity();

            actor.setX((float) (x + vel * Math.cos(rad)));
            actor.setY((float) (y + vel * Math.sin(rad)));
        }
    }
}
