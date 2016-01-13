package com.elano.projetosabin;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private HorizontalScrollView horizontalScrollView;
    private RelativeLayout relativeContainer;
    private ImageView imageView;
    private float density;
    private float scaleY;
    private float scaleX;
    private int imagePointSize;
    private int halfImagePointSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        density = (metrics.densityDpi / 160f);
        imagePointSize = (int) (50 * density);
        halfImagePointSize = imagePointSize/2;

        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        relativeContainer = (RelativeLayout) findViewById(R.id.relative_container);
        imageView = (ImageView) findViewById(R.id.imageView);

        ViewTreeObserver vto = imageView.getViewTreeObserver();

        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                int finalHeight = imageView.getMeasuredHeight();
                int finalWidth = imageView.getMeasuredWidth();


                scaleY = finalHeight / 480.0f;
                scaleX = finalWidth / 1048.0f;


                Log.d("", "Height: " + finalHeight + " Width: " + finalWidth + " scaleY:" + scaleY);
                addPoints(129, 64);
                addPoints(759, 319);
                return true;
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("", "onOptionsItemSelected | main");
        switch (item.getItemId()) {
            case R.id.action_achar:
                discover();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void discover() {
        focusOnView(horizontalScrollView, findViewById(R.id.imageButton));
    }

    private void addPoints(int a_x, int a_y) {

        ImageButton button = new ImageButton(this);
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();

        button.setBackgroundResource(R.mipmap.location);
        //button.setBackgroundColor(Color.BLACK);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imagePointSize, imagePointSize);

        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        params.leftMargin = convertToRealXPosition(a_x) - halfImagePointSize;
        params.topMargin = convertToRealYPosition(a_y) - imagePointSize; // -8: image fix
        relativeContainer.addView(button, params);
    }

    private int convertToRealXPosition(float position) {
        return (int) (position * scaleX); //* density
    }

    private int convertToRealYPosition(float position) {
        return (int) (position * scaleY); //* density
    }

    private final void focusOnView(final HorizontalScrollView scroll, final View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int vLeft = view.getLeft();
                int vRight = view.getRight();
                int sWidth = scroll.getWidth();
                scroll.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
            }
        });
    }
}
