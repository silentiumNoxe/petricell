package com.silentiumnoxe.game.petricell.component;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KVLabel extends Label {

    private CharSequence key;
    private CharSequence value;
    private BitmapFont font;

    public KVLabel(final CharSequence key, final CharSequence value, final BitmapFont font) {
        super("%s: %s".formatted(key, value), new LabelStyle(font, font.getColor()));
        this.key = key;
        this.value = value;
        this.font = font;
    }

    public KVLabel(final CharSequence key, final BitmapFont font) {
        this(key, "", font);
    }

    public void setKey(final CharSequence key) {
        this.key = key;
        setText("%s: %s".formatted(key, value));
    }

    public void setValue(final Object value) {
        this.value = String.valueOf(value);
        setText("%s: %s".formatted(key, this.value));
    }
}
