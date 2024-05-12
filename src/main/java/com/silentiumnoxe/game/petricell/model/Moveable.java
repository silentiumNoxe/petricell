package com.silentiumnoxe.game.petricell.model;

public interface Moveable {

    float getVelocity();
    void setVelocity(float value);

    int getAngle();
    void setAngle(int value);

    default double getAngleRad() {
        return Math.toRadians(getAngle());
    }

    default void setAngleRad(double value) {
        setAngle((int) Math.toDegrees(value));
    }
}
