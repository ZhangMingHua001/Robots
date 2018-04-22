package com.zmh.animation.robots;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;

class StartRobotAnimator extends ValueAnimator implements IRobotsAnimator, ValueAnimator.AnimatorUpdateListener {

    private RobotDrawable mDrawable;
    private int DURATION = 4000;


    StartRobotAnimator(Context context) {
        this.setDuration(DURATION);
        this.setIntValues(0, DURATION);
        this.setInterpolator(new LinearInterpolator());
        mDrawable = new RobotDrawable(context);
        addUpdateListener(this);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mDrawable.update((int) (animation.getAnimatedValue()));
        mDrawable.invalidateSelf();
    }

    @Override
   public Drawable getDrawable() {
        return mDrawable;
    }

    private class RobotDrawable extends Drawable {

        private int mTime;
        private Body mBody;
        private EyeLeft mEyeLeft;
        private EyeRight mEyeRight;
        private HandLeft mHandLeft;
        private HandRight mHandRight;
        private Head mHead;
        private Mouth mMouth;
        private int mWidth;
        private int mHeight;

        RobotDrawable(Context context) {
            mBody = new Body(context);
            mEyeLeft = new EyeLeft(context);
            mEyeRight = new EyeRight(context);
            mHandLeft = new HandLeft(context);
            mHandRight = new HandRight(context);
            mHead = new Head(context);
            mMouth = new Mouth(context);

        }


        @Override
        public void setAlpha(int alpha) {

        }

        private void update(int time) {
            mTime = time;
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            if (mWidth == 0 || mHeight == 0) {
                mWidth = canvas.getWidth();
                mHeight = canvas.getHeight();
            }
            canvas.save();
            canvas.translate(mWidth / 2f - 126 / 2, mHeight / 2f - 164 / 2);

            float T0 = DURATION;
            float t = Math.min(mTime, DURATION);
            double translateY = (-30f * t / T0 + 30) * Math.sin(3.5 * Math.PI / T0 * t + Math.PI / 2f);
            canvas.translate(0, (float) translateY);

            mHead.draw(canvas);
            mMouth.draw(canvas, mTime);
            mHandLeft.draw(canvas, mTime);
            mHandRight.draw(canvas, mTime);

            mBody.draw(canvas);
            mEyeLeft.draw(canvas, mTime);
            mEyeRight.draw(canvas, mTime);

            canvas.restore();
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    }

    private class Body extends Basic {

        Body(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.body);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, 57, 59, true);
        }

        private void draw(Canvas canvas) {
            mMatrix.setTranslate(34, 103);
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class EyeLeft extends Eye {

        EyeLeft(Context context) {
            mTransX = 28;
            mTransY = 57;
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.eye_left);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, 18, 22, true);
            mBmpSmall = BitmapFactory.decodeResource(context.getResources(), R.drawable.small_eye_left);
            mBmpSmall = Bitmap.createScaledBitmap(mBmpSmall, 17, 5, true);
        }
    }

    private class EyeRight extends Eye {


        EyeRight(Context context) {
            mTransX = 80;
            mTransY = 57;
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.eye_right);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, 18, 22, true);

            mBmpSmall = BitmapFactory.decodeResource(context.getResources(), R.drawable.small_eye_right);
            mBmpSmall = Bitmap.createScaledBitmap(mBmpSmall, 17, 5, true);
        }
    }

    class Eye extends Basic {
        Bitmap mBmpSmall;
        Matrix mSmall = new Matrix();
        final float[] mTimes = {50f, 200f};
        final float[] mProgress = {1f, 0.15f, 1f};
        final Interpolator[] mInterpolator = {
                new PathInterpolator(0.33f, 0.00f, 0.67f, 1.00f),
                new PathInterpolator(0.33f, 0.00f, 0.67f, 1.00f)
        };

        Interpolator mStartInterpolator = new PathInterpolator(0.03f, 0.59f, 0.49f, 1.00f);
        int mTransX;
        int mTransY;

        void draw(Canvas canvas, int time) {
            mMatrix.setTranslate(mTransX, mTransY);
            mSmall.setTranslate(mTransX + 1, mTransY + mBitmap.getHeight() / 2f + 1);
            if (time <= 350) {
                float progress = mStartInterpolator.getInterpolation(time / 350f);
                mMatrix.postScale(1, progress, mTransX + mBitmap.getWidth() / 2f, mTransY + mBitmap.getHeight() / 2f);
                canvas.drawBitmap(mBmpSmall, mSmall, null);
            }

            if (time >= 2350 && time <= 2600) {
                float scale = getProgress(time - 2350, mTimes, mProgress, mInterpolator);
                mMatrix.postScale(1, scale, mTransX + mBitmap.getWidth() / 2f, mTransY + mBitmap.getHeight() / 2f);
            }
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class HandLeft extends Basic {

        private Interpolator interpolator = new PathInterpolator(0.21f, 0.00f, 0.29f, 1.00f);
        private int DURATION = 750;

        HandLeft(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand_left);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, 19, 27, true);
        }

        private void draw(Canvas canvas, int time) {
            time = Math.min(time, DURATION);
            float progress = interpolator.getInterpolation(time * 1f / DURATION) * 60f - 60f;
            mMatrix.setTranslate(16, 116);
            mMatrix.postRotate(progress, 37, 114);
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class HandRight extends Basic {
        private Interpolator interpolator = new PathInterpolator(0.21f, 0.00f, 0.29f, 1.00f);
        private int DURATION = 750;

        HandRight(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand_right);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, 19, 27, true);
        }

        private void draw(Canvas canvas, int time) {
            time = Math.min(time, DURATION);
            float progress = 60f - interpolator.getInterpolation(time * 1f / DURATION) * 60f;
            mMatrix.setTranslate(90, 116);
            mMatrix.postRotate(progress, 88, 114);
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class Head extends Basic {
        Head(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, 126, 103, true);
        }

        private void draw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }

    }

    private class Mouth extends Basic {
        private final Paint mPaint = new Paint();

        Mouth(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mouth);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, 12, 8, true);
        }

        private void draw(Canvas canvas, int time) {
            time = Math.min(Math.max(0, time - 200), 150);
            mPaint.setAlpha(255 * time / 150);
            mMatrix.setTranslate(57, 81);
            canvas.drawBitmap(mBitmap, mMatrix, mPaint);

        }
    }

    private class Basic {

        Bitmap mBitmap;
        Matrix mMatrix = new Matrix();

        float getProgress(int time, float[] times, float[] progresss, Interpolator[] interpolators) {
            float progress = 0f;
            float t;
            if (time <= times[0]) {
                t = 1f * time / times[0];
                progress = progresss[0] + interpolators[0].getInterpolation(t) * (progresss[1] - progresss[0]);
            } else if (time <= times[0] + times[1]) {
                t = 1f * (time - times[0]) / times[1];
                progress = progresss[1] + interpolators[1].getInterpolation(t) * (progresss[2] - progresss[1]);
            } else if (time <= times[0] + times[1] + times[2]) {
                t = 1f * (time - times[1] - times[0]) / times[2];
                progress = progresss[2] + interpolators[2].getInterpolation(t) * (progresss[3] - progresss[2]);
            } else if (time <= times[0] + times[1] + times[2] + times[3]) {
                t = 1f * (time - times[2] - times[1] - times[0]) / times[3];
                progress = progresss[3] + interpolators[3].getInterpolation(t) * (0 - progresss[3]);
            }
            return progress;
        }
    }

}
