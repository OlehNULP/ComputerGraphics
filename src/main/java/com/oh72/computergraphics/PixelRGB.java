package com.oh72.computergraphics;

/**
 * @author Oleh Hembarovskyi
 * @link oleh.hembarovskyi@embrox.com
 * @since 23/11/2022
 **/
public class PixelRGB {
    Double red;
    Double green;
    Double blue;

    public PixelRGB(Double red, Double green, Double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Double getRed() {
        return red;
    }

    public void setRed(Double red) {
        this.red = red;
    }

    public Double getGreen() {
        return green;
    }

    public void setGreen(Double green) {
        this.green = green;
    }

    public Double getBlue() {
        return blue;
    }

    public void setBlue(Double blue) {
        this.blue = blue;
    }
}
