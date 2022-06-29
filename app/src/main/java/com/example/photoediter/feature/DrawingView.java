package com.example.photoediter.feature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DrawingView extends View {
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private float mX, mY;
    private Bitmap bitmap;
    private static final float TOUCH_TOLERANCE = 4;
    private ArrayList<Path> paths = new ArrayList<Path>();
    private final ArrayList<Path> undoPaths = new ArrayList<Path>();
    private final ArrayList<Paint> listPaint = new ArrayList<Paint>();
    private boolean isDraw = false;
    private int size = 0, color = 0;

    public DrawingView(Context context) {
        this(context, null);
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setLayerType(LAYER_TYPE_HARDWARE, null);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        if (color != 0) mPaint.setColor(color);
        else mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        if (size != 0) mPaint.setStrokeWidth(size);
        else mPaint.setStrokeWidth(10);
        mCanvas = new Canvas();
        mPath = new Path();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
    }

    public void clearAll() {
        if (mPaint != null && mPaint != null) {
            paths.clear();
            undoPaths.clear();
            listPaint.clear();
            mPath.reset();
            mPaint.reset();
            invalidate();
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), listPaint.get(i));
        }
    }

    public Bitmap createBitmap() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        return bitmap;
    }


    private void touch_start(float x, float y) {
        init();
        paths.add(mPath);
        listPaint.add(mPaint);
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);

        // kill this so we don't double draw
//        mPath = new Path();
//        paths.add(mPath);
    }

    public void onClickUndo() {
        if (paths.size() > 0) {
            undoPaths.add(paths.remove(paths.size() - 1));
            invalidate();
        }
    }

    public void onClickRedo() {
        if (undoPaths.size() > 0) {
            paths.add(undoPaths.remove(undoPaths.size() - 1));
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (isDraw) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();

                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();

                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();

                    break;
            }
        }
        return true;
    }

}
