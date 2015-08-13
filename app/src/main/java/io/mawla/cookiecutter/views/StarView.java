package io.mawla.cookiecutter.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class StarView extends View {
    Paint paint = new Paint();


    public StarView(Context context) {
        super(context);
    }

    public StarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        canvas.drawColor(Color.TRANSPARENT);

        Path path = createStar(5, new Point(canvas.getWidth()/2, canvas.getHeight()/2), 100, 40);

        path.close();

        canvas.clipPath(path);
        canvas.drawPath(path, paint);
    }

    public static Path createStar(int arms, Point center, double rOuter, double rInner)
    {
        double angle = Math.PI / arms;

        Path path = new Path();

        for (int i = 0; i < 2 * arms; i++)
        {
            double r = (i & 1) == 0 ? rOuter : rInner;
            float x = (float)(center.x + Math.cos(i * angle) * r);
            float y = (float)(center.y + Math.sin(i * angle) * r);
            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        return path;
    }
}