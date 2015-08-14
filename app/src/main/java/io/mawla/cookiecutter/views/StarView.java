package io.mawla.cookiecutter.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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

    public static Bitmap createStarBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        Path path = StarView.createStar(5, new Point(bitmap.getWidth() / 2, bitmap.getHeight() / 2), 130, 50);

        path.close();

        canvas.clipPath(path);
        canvas.drawPath(path, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}