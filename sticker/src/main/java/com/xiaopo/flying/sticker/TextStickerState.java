package com.xiaopo.flying.sticker;

import android.graphics.Paint;
import android.graphics.Typeface;

public class TextStickerState {
    private int color;
    private int colorBackground;
    private Typeface font;


    public TextStickerState() {
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
    }

    public Typeface getFont() {
        return font;
    }

    public void setFont(Typeface font) {
        this.font = font;
    }

    public TextStickerState(int color, int colorBackground, Typeface font) {
        this.color = color;
        this.colorBackground = colorBackground;
        this.font = font;
    }
}
