package io.mawla.cookiecutter;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.mawla.cookiecutter.views.OvalView;
import io.mawla.cookiecutter.views.StarView;

public class MainActivity extends AppCompatActivity {
    StarView starView;
    OvalView ovalView;

    View activeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup mLinearLayout = (ViewGroup) findViewById(R.id.activity_main123);

        TextView view = new TextView(this);
        view.setText("SUPER TEEEEST");

        starView = new StarView(this);
        starView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        starView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeView = v;
            }
        });

        ovalView = new OvalView(this);
        ovalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeView = v;
            }
        });

        ovalView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));

        ovalView.setX(100);

        ovalView.setOnLongClickListener(new CutterLongClickListener());

        starView.setOnLongClickListener(new CutterLongClickListener());


        // Sets the drag event listener for the View
        myDragEventListener mDragListen = new myDragEventListener();
//        myDragEventListener mDragListen2 = new myDragEventListener();

        ovalView.setOnDragListener(mDragListen);
//        starView.setOnDragListener(mDragListen2);

        mLinearLayout.addView(ovalView);
        mLinearLayout.addView(starView);

        mLinearLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:

                        // Turns off any color tinting
                        if (activeView != null) {
                            activeView.setX(event.getX() - 100);
                            activeView.setY(event.getY() - 100);
                        }

                        // Invalidates the view to force a redraw
                        v.invalidate();

                        return true;
                }
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        }

        return super.onTouchEvent(event);
    }

    protected class myDragEventListener implements View.OnDragListener {

        // This is the method that the system calls when it dispatches a drag event to the
        // listener.
        public boolean onDrag(View v, DragEvent event) {

            // Defines a variable to store the action type for the incoming event
            final int action = event.getAction();

            // Handles each of the expected events
            switch (action) {

                case DragEvent.ACTION_DRAG_STARTED:

                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        // As an example of what your application might do,
                        // applies a blue color tint to the View to indicate that it can accept
                        // data.

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        // returns true to indicate that the View can accept the dragged data.
                        return true;

                    }

                    // Returns false. During the current drag and drop operation, this View will
                    // not receive events again until ACTION_DRAG_ENDED is sent.
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:

                    // Applies a green tint to the View. Return true; the return value is ignored.


                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:

                    // Ignore the event
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:

                    // Re-sets the color tint to blue. Returns true; the return value is ignored.

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DROP:

                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    // Gets the text data from the item.
                    String text = item.getText().toString();

                    // Displays a message containing the dragged data.
                    Toast.makeText(getApplicationContext(), "Dragged data is " + text + "vs" + event.getX() + ":" + event.getY(), Toast.LENGTH_LONG).show();

                    v.setX(event.getX());
                    v.setY(event.getY());

                    // Turns off any color tints
//                    v.clearColorFilter();

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    // Turns off any color tinting
//                    v.clearColorFilter();

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Does a getResult(), and displays what happened.
                    if (event.getResult()) {
                        Toast.makeText(getApplicationContext(), "The drop was handled.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "The drop didn't work.", Toast.LENGTH_LONG).show();

                    }

                    // returns true; the value is ignored.
                    return true;

                // An unknown action type was received.
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

        // Defines the one method for the interface, which is called when the View is long-clicked
        public boolean onLongClick(View v) {
            activeView = v;
            // Create a new ClipData.
            // This is done in two steps to provide clarity. The convenience method
            // ClipData.newPlainText() can create a plain text ClipData in one step.

            // Create a new ClipData.Item from the ImageView object's tag
            ClipData.Item item = new ClipData.Item(v.getX() + ":" + v.getY());

            // Create a new ClipData using the tag as a label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            ClipData dragData = new ClipData("WOO", new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

            // Instantiates the drag shadow builder.
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);

            // Starts the drag

            v.startDrag(dragData,  // the data to be dragged
                    myShadow,  // the drag shadow builder
                    null,      // no need to use local data
                    0          // flags (not currently used, set to 0)
            );

            return true;

        }

    }
}
