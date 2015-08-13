package io.mawla.cookiecutter;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import io.mawla.cookiecutter.views.RectangleView;
import io.mawla.cookiecutter.views.StarView;

public class MainActivity extends AppCompatActivity {
    StarView starView;
    RectangleView rectangleView;
    Bitmap bitmap;
    View activeView;
    ViewGroup mLinearLayout;
    ViewGroup dockLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLinearLayout = (ViewGroup) findViewById(R.id.activity_main123);
        dockLayout = (ViewGroup) findViewById(R.id.activity_main_dock);
        ViewTreeObserver vto = mLinearLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 6;

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cookies, options);
                bitmap = Bitmap.createScaledBitmap(bitmap, mLinearLayout.getWidth(), mLinearLayout.getHeight(), false);
                mLinearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        starView = new StarView(this);
        starView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        starView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeView = v;
            }
        });
        rectangleView = new RectangleView(this);
        rectangleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeView = v;
            }
        });
        rectangleView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        rectangleView.setX(100);
        rectangleView.setOnLongClickListener(new CutterLongClickListener());
        starView.setOnLongClickListener(new CutterLongClickListener());
        myDragEventListener mDragListen = new myDragEventListener();
        rectangleView.setOnDragListener(mDragListen);
        mLinearLayout.addView(rectangleView);
        mLinearLayout.addView(starView);
        mLinearLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        if (activeView != null) {
                            activeView.setX(event.getX() - 100);
                            activeView.setY(event.getY() - 100);
                            addCroppedImageToView(dockLayout, activeView);
                        }
                        v.invalidate();
                        return true;
                }
                return true;
            }
        });

        activeView = rectangleView;
    }

    private void addCroppedImageToView(final ViewGroup mLinearLayout, View cropper) {
        Bitmap croppedBmp;
        try {
            if(activeView.getClass() == RectangleView.class){
                croppedBmp = Bitmap.createBitmap(bitmap, (int) cropper.getX(), (int) cropper.getY(), cropper.getWidth(), cropper.getHeight());
            }else{
                Bitmap croppedBmp123 = Bitmap.createBitmap(bitmap, (int) cropper.getX(), (int) cropper.getY(), cropper.getWidth(), cropper.getHeight());
                croppedBmp = createStarBitmap(croppedBmp123);
            }
        } catch (Exception e) {
            return;
        }

        final ImageView image = new ImageView(getApplicationContext());
        image.setImageBitmap(croppedBmp);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        image.setScaleType(ImageView.ScaleType.FIT_XY);

        params.setMargins(5, 5, 5, 5);
        image.setLayoutParams(params);
        setImageViewRemovalAnimation(image, mLinearLayout);
        addImageToViewWithAnimation(mLinearLayout, image);
    }

    private void setImageViewRemovalAnimation(final ImageView image, final ViewGroup dock) {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ScaleAnimation animation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f);
                animation.setDuration(500);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dock.removeView(v);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                image.startAnimation(animation);
            }
        });
    }

    private void addImageToViewWithAnimation(ViewGroup mLinearLayout, ImageView image) {
        image.setVisibility(View.INVISIBLE);
        image.animate().translationX(-50);
        mLinearLayout.addView(image);
        image.setVisibility(View.VISIBLE);
        image.animate().translationY(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (activeView != null) {
            activeView.setX(event.getX() - 100);
            activeView.setY(event.getY() - 350);
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    addCroppedImageToView(dockLayout, activeView);
            }
        }
        return super.onTouchEvent(event);
    }

    protected class myDragEventListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {


                        v.invalidate();
                        return true;
                    }

                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String text = item.getText().toString();

                    v.setX(event.getX());
                    v.setY(event.getY());
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.TRANSPARENT);
                    v.invalidate();
                    if (event.getResult()) {

                    } else {

                    }
                    return true;
                default:
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                    break;
            }
            return false;
        }
    }

    public class CutterLongClickListener implements View.OnLongClickListener {
        public CutterLongClickListener() {
        }

        public boolean onLongClick(View v) {
            activeView = v;


            ClipData.Item item = new ClipData.Item(v.getX() + ":" + v.getY());


            ClipData dragData = new ClipData("WOO", new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);
            v.startDrag(dragData,
                    myShadow,
                    null,
                    0
            );
            return true;
        }
    }

    public Bitmap createStarBitmap(Bitmap bitmap) {
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
