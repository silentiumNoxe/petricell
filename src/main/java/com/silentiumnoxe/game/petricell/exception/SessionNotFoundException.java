package com.silentiumnoxe.game.petricell.exception;

public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException(final String key) {
        super("Session not found: " + key);
    }
}
