package io.mawla.cookiecutter.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import io.mawla.cookiecutter.R;

public class StarView extends View {
    Paint paint = new Paint();

    Path path = new Path();

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

        float mid = getWidth() / 2;
        float min = Math.min(getWidth(), getHeight());
        float half = min / 2;
        mid = mid - half;

        canvas.drawColor(Color.TRANSPARENT);

        paint.setStyle(Paint.Style.FILL);

        applyStarToPath(mid, half);

        path.close();
        canvas.drawPath(path, paint);

    }

    private void applyStarToPath(float mid, float half) {
        path.moveTo(mid + half * 0.5f, half * 0.84f);
        path.lineTo(mid + half * 1.5f, half * 0.84f);
        path.lineTo(mid + half * 0.68f, half * 1.45f);
        path.lineTo(mid + half * 1.0f, half * 0.5f);
        path.lineTo(mid + half * 1.32f, half * 1.45f);
        path.lineTo(mid + half * 0.5f, half * 0.84f);
    }
}