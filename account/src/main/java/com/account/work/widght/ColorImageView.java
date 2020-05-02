package com.account.work.widght;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * The icon of the bill type uses this custom ColorImageView
 * Explanation: our style is that the icon has a round background, and the background can dynamically modify the color
 * So customize this imageView, which has a round background, and can easily change the background color
 */

public class ColorImageView extends ImageView {

    int bgColor = -1;
    Paint bgPaint;
    private int height;
    private int width;
    private int cx;
    private int cy;
    private int radius;

    public ColorImageView(Context context) {
        super(context);
        initPaint();
    }

    public ColorImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setDither(true);
        bgPaint.setColor(Color.TRANSPARENT);
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
        width = getMeasuredWidth();

        cx = width / 2;
        cy = height / 2;
        radius = height > width ? width / 2 : height / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (bgColor != -1) {
            bgPaint.setColor(bgColor);
        } else {
            bgPaint.setColor(Color.TRANSPARENT);
        }

        canvas.drawCircle(cx, cy, radius, bgPaint);
        super.onDraw(canvas);
    }

    public void setBgColor(int color) {
        bgColor = color | 0xFF000000;
        bgPaint.setColor(bgColor);
    }

    public int getBgColor() {
        return bgColor;
    }
}
