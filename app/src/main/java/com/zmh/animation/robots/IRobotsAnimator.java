package com.zmh.animation.robots;

import android.graphics.drawable.Drawable;

interface IRobotsAnimator {
    Drawable getDrawable();

    void start();

    void cancel();

}
