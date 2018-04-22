package com.zmh.animation.robots;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
    private ImageView mImageView;
    private IRobotsAnimator mAnimator;

    private final static int TYPE_START = 0x1;
    private final static int TYPE_WAVE_LEFT = 0x2;
    private final static int TYPE_WAVE_RIGHT = 0x3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.img);
        mAnimator = new StartRobotAnimator(this.getApplicationContext());
        mImageView.setImageDrawable(mAnimator.getDrawable());

        findViewById(R.id.btn_wave_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimator(TYPE_WAVE_LEFT);

            }
        });

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimator(TYPE_START);
            }
        });

        findViewById(R.id.btn_wave_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimator(TYPE_WAVE_RIGHT);
            }
        });
    }


    private void startAnimator(int type) {
        cancelAnimator();
        switch (type) {
            case TYPE_START:
                mAnimator = new StartRobotAnimator(this.getApplicationContext());
                break;
            case TYPE_WAVE_LEFT:
                mAnimator = new WaveRobotAnimator(this.getApplicationContext(), true);
                break;
            case TYPE_WAVE_RIGHT:
                mAnimator = new WaveRobotAnimator(this.getApplicationContext(), false);
                break;
            default:
                mAnimator = new StartRobotAnimator(this.getApplicationContext());
                break;
        }
        mImageView.setImageDrawable(mAnimator.getDrawable());
        mAnimator.start();
    }

    private void cancelAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAnimator();
    }
}
