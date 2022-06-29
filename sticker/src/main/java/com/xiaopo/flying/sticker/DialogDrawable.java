package com.xiaopo.flying.sticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.BoringLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;

import java.util.ArrayList;
import java.util.List;


public class DialogDrawable extends Drawable {

    private Paint mBorderPaint;
    private Paint mTextPaint;
    private RectF mRectF;
    private RectF background;
    private Paint paintBackground;
    private int mRadius = 20;
    private int width = 0;
    private int height = 0;
    private TextStickerState state;
    public List<TextStickerState> list = new ArrayList<>();
    public List<TextStickerState> redoList = new ArrayList<>();
    private String textDraw = "";
    public static boolean isDelete = false;
    private String colorShadow = "000000";
    private String font = "";

    private String gradientTop = "ffffff";
    private String gradientBottom = "ffffff";

    private String stroke = "000000";
    private boolean inEdit = true;

    public void setInEdit(boolean inEdit) {
        this.inEdit = inEdit;
    }

    private int currentColor;

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    @SuppressLint("ResourceType")
    public DialogDrawable(Context context) {
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(Color.parseColor("#00000000"));

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(50);

        paintBackground = new Paint();
        paintBackground.setColor(Color.TRANSPARENT);
        paintBackground.setStyle(Paint.Style.FILL);

//        mRectF = new RectF(0, 0, getIntrinsicWidth() - mOffsetX, getIntrinsicHeight() - mOffsetY);\
        textDraw = "double_click";
        state = new TextStickerState(mTextPaint.getColor(), paintBackground.getColor(), mTextPaint.getTypeface());
        list.add(state);
        resizeText();
    }

    public String getTextDraw() {
        return textDraw;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);

    }

    @Override
    public void draw(Canvas canvas) {
        for (int j = 0; j < list.size(); j++) {
            if (inEdit)
                canvas.drawRoundRect(mRectF, mRadius, mRadius, mBorderPaint);

            canvas.drawRect(-5, -5, width + 5, height + 5, paintBackground);

            float[] characterWidths = new float[textDraw.length()];
            int characterNum = mTextPaint.getTextWidths(textDraw, characterWidths);

            float textWidth = 0f;
            for (int i = 0; i < characterNum; i++) {
                textWidth += characterWidths[i];
            }
            float y = mRectF.centerY();
            canvas.save();
            canvas.translate(mRectF.width() / 2 - textWidth / 2, 0);
            canvas.drawText(textDraw, 0, y, mTextPaint);

            canvas.restore();
        }

    }

    public void delete() {
        list.clear();
        redoList.clear();
    }

    public void undo() {
        if (list.size() > 0) {
            redoList.add(list.get(list.size() - 1));
            list.remove(list.get(list.size() - 1));
            if (list.size() > 0) {
                isDelete = false;
                mTextPaint.setColor(list.get(list.size() - 1).getColor());
                mTextPaint.setTypeface(list.get(list.size() - 1).getFont());
                paintBackground.setColor(list.get(list.size() - 1).getColorBackground());
                resizeText();
            } else isDelete = true;
        }
    }

    public void redo() {
        if (redoList.size() > 0) {
            mTextPaint.setColor(redoList.get(redoList.size() - 1).getColor());
            mTextPaint.setTypeface(redoList.get(redoList.size() - 1).getFont());
            paintBackground.setColor(redoList.get(redoList.size() - 1).getColorBackground());
            list.add(redoList.get(redoList.size() - 1));
            redoList.remove(redoList.get(redoList.size() - 1));
            resizeText();
        }
    }


    @Override
    public void setAlpha(int i) {
        mBorderPaint.setAlpha(i);
        mTextPaint.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public int getOpacity() {
        return 0;
    }

    public void setColorBackground(int colorBackground) {
        paintBackground.setColor(colorBackground);
        resizeText();
        state = new TextStickerState();
        state.setColorBackground(paintBackground.getColor());
        state.setColor(list.get(list.size() - 1).getColor());
        state.setFont(list.get(list.size() - 1).getFont());
        list.add(state);
    }

    public int getBackground() {
        return paintBackground.getColor();
    }

    public Typeface getFontText() {
        return mTextPaint.getTypeface();
    }

    private static final String TAG = "DialogDrawable";

    public void setText(String valueText) {
        textDraw = valueText;
        resizeText();
    }

    public void setFontText(Typeface intColor) {
        mTextPaint.setTypeface(intColor);
        resizeText();
        state = new TextStickerState();
        state.setColorBackground(list.get(list.size() - 1).getColorBackground());
        state.setColor(list.get(list.size() - 1).getColor());
        state.setFont(mTextPaint.getTypeface());
        list.add(state);
    }

    public void setColorText(int intColor) {
        mTextPaint.setColor(intColor);
        resizeText();
        state = new TextStickerState();
        state.setColorBackground(list.get(list.size() - 1).getColorBackground());
        state.setFont(list.get(list.size() - 1).getFont());
        state.setColor(intColor);
        list.add(state);
    }

    private void resizeText() {
        height = 0;
        width = 0;
        Paint paint = new Paint();
        Rect bounds = new Rect();
        paint.setTextSize(56);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paint.setLetterSpacing(mTextPaint.getLetterSpacing());
        }
        paint.setTypeface(mTextPaint.getTypeface());
        paint.getTextBounds(textDraw, 0, textDraw.length(), bounds);

        String[] strings = textDraw.split("\n");
        int size = strings.length;

        float textWidth = 0f;
        if (size == 1) {
            textWidth = getWidthString(textDraw);
        } else {
            float wCheck = 0;
            for (String string : strings) {
                wCheck = getWidthString(string);
                textWidth = Math.max(textWidth, wCheck);
            }

        }
        width = (int) (textWidth + 30);
        for (int i = 0; i < size; i++) {
            height += bounds.height() + 20;
        }

        mRectF = new RectF(0, 30, width, height);
        background = new RectF(0, 0, width, height);

        Log.d(TAG, "resizeText: " + height);
    }

    private float getWidthString(String s) {
        float textWidth = 0f;
        float[] characterWidths = new float[s.length()];
        int characterNum = mTextPaint.getTextWidths(s, characterWidths);
        for (int i = 0; i < characterNum; i++) {
            textWidth += characterWidths[i];
        }
        return textWidth;
    }

    public int getColor() {
        return mTextPaint.getColor();
    }

}
