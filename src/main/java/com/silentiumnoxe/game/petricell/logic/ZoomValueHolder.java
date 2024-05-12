package com.silentiumnoxe.game.petricell.logic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZoomValueHolder {

    public static long MAX_ZOOM = 100; // 100 nanometers
    public static long MIN_ZOOM = 10000000; // 1 centimeter

    private static ZoomValueHolder instance;

    public static ZoomValueHolder getInstance() {
        if (instance == null) {
            instance = new ZoomValueHolder();
        }
        return instance;
    }

    // value in nanometers
    private long zoom = MIN_ZOOM;

    public long getZoomNano() {
        return zoom;
    }

    public long getZoomMicro() {
        return getZoomNano() / 1000;
    }

    public long getZoomMilli() {
        return getZoomMicro() / 1000;
    }

    public void setZoom(final long zoom) {
        if (zoom < MAX_ZOOM) {
            this.zoom = MAX_ZOOM;
            return;
        }

        if (zoom > MIN_ZOOM) {
            this.zoom = MIN_ZOOM;
            return;
        }

        this.zoom = zoom;
    }
}
