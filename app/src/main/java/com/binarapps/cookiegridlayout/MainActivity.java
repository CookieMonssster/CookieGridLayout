package com.binarapps.cookiegridlayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button animationTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animationTool = (Button) findViewById(R.id.animationTool);
        Button button = (Button) findViewById(R.id.button_8);
        button.setOnTouchListener((view, motionEvent) -> {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    startScaleAnimation(view);
                    break;
            }

            return false;
        });
    }

    private void startScaleAnimation(View view) {

        int x = Math.round(view.getX());
        int y = Math.round(view.getY());
        animationTool.layout(x, y, x + view.getWidth(), y + view.getHeight());
        animationTool.setBackground(view.getBackground());

        ScaleAnimation scale = new ScaleAnimation(0,10,0,10, view.getWidth()/2, view.getHeight()/2);
        scale.setDuration(250);
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startNewActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationTool.startAnimation(scale);
    }

    private void startNewActivity() {
        Intent intent = new Intent(getApplicationContext(), SampleActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
