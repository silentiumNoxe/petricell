package com.silentiumnoxe.game.petricell.exception;

public class ScreenNotFoundException extends RuntimeException {

    public ScreenNotFoundException(final String name) {
        super("Screen %s not found".formatted(name));
    }
}
