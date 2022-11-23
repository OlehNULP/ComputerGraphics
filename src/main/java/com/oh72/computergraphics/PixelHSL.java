package com.oh72.computergraphics;

/**
 * @author Oleh Hembarovskyi
 * @link oleh.hembarovskyi@embrox.com
 * @since 23/11/2022
 **/
public class PixelHSL {
    Double hue;
    Double saturation;
    Double lightness;

    public PixelHSL(Double hue, Double saturation, Double lightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
    }

    public Double getHue() {
        return hue;
    }

    public void setHue(Double hue) {
        this.hue = hue;
    }

    public Double getSaturation() {
        return saturation;
    }

    public void setSaturation(Double saturation) {
        this.saturation = saturation;
    }

    public Double getLightness() {
        return lightness;
    }

    public void setLightness(Double lightness) {
        this.lightness = lightness;
    }
}
