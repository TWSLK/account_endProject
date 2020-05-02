package com.account.work.utils.depend;

import android.animation.TypeEvaluator;
import android.graphics.Point;

/**
 * Bessel curve (two order parabola)
 * controllPoint It's a turning point in the middle
 * startValue Is the starting position
 * endValue Is the end of the position
 */
public class BizierEvaluator2 implements TypeEvaluator<Point> {

    private Point controllPoint;

    public BizierEvaluator2(Point controllPoint) {
        this.controllPoint = controllPoint;
    }

    @Override
    public Point evaluate(float t, Point startValue, Point endValue) {
        int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controllPoint.x + t * t * endValue.x);
        int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controllPoint.y + t * t * endValue.y);
        return new Point(x, y);
    }
}