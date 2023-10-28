package com.lucky.blocks.mods.mcpeaddons;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

@SuppressWarnings("ALL")
public class CustomTypefaceSpan extends TypefaceSpan {
    private final Typeface newType;

    public CustomTypefaceSpan(String str, Typeface typeface) {
        super(str);
        this.newType = typeface;
    }

    private static void applyCustomTypeFace(Paint paint, Typeface typeface) {
        Typeface typeface2 = paint.getTypeface();
        int style = (typeface2 == null ? 0 : typeface2.getStyle()) & (~typeface.getStyle());
        if ((style & 1) != 0) {
            paint.setFakeBoldText(true);
        }
        if ((style & 2) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(typeface);
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        applyCustomTypeFace(textPaint, this.newType);
    }

    @Override
    public void updateMeasureState(TextPaint textPaint) {
        applyCustomTypeFace(textPaint, this.newType);
    }
}
