package com.silentiumnoxe.game.petricell.util;

import java.util.function.Function;

public class SizeFormatter implements Function<Long, String> {

    private static final SizeFormatter INSTANCE = new SizeFormatter();

    public static String applyStatic(final Long value) {
        return INSTANCE.apply(value);
    }

    @Override
    public String apply(final Long value) {
        float x = value;
        if (x < 1000) {
            return "%.0f nm".formatted(x);
        }
        x = x / 1000;

        if (x < 1000) {
            return "%.3f µm".formatted(x);
        }
        x = x / 1000;

        return "%.2f mm".formatted(x);
    }
}
