package com.hencoder.a12_scalable;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

//这个存在问题，放大后移动，再缩小，会有便宜，因为放大移动，offsetX变了（解决方案是偏移跟缩放挂钩）
public class ScalableImageView2 extends View {
    private static final float IMAGE_WIDTH = Utils.dpToPixel(300);
    private static final float OVER_SCALE_FACTOR = 1.f;

    Bitmap bitmap;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    float offsetX;
    float offsetY;
    float originalOffsetX;
    float originalOffsetY;
    float smallScale;
    float bigScale;
    boolean big;
    float currentScale;
    ObjectAnimator scaleAnimator;
    GestureDetectorCompat detector;
    HenGestureListener gestureListener = new HenGestureListener();
    HenFlingRunner henFlingRunner = new HenFlingRunner();
    OverScroller scroller;
    private float mScaleFraction;

    public ScalableImageView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap = Utils.getAvatar(getResources(), (int) IMAGE_WIDTH);
        detector = new GestureDetectorCompat(context, gestureListener);
        scroller = new OverScroller(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        originalOffsetX = ((float) getWidth() - bitmap.getWidth()) / 2;
        originalOffsetY = ((float) getHeight() - bitmap.getHeight()) / 2;

        //图片的宽高比与屏幕的宽高比（像本张图片1>0.7）
        if ((float) bitmap.getWidth() / bitmap.getHeight() > (float) getWidth() / getHeight()) {
            //smallScale 宽度贴边，OVER_SCALE_FACTOR =1时bigScale是高度贴边
            //走进这里，说明宽度放大到与屏幕宽度比较快
            smallScale = (float) getWidth() / bitmap.getWidth();
            bigScale = (float) getHeight() / bitmap.getHeight() * OVER_SCALE_FACTOR;
        } else {
            smallScale = (float) getHeight() / bitmap.getHeight();
            bigScale = (float) getWidth() / bitmap.getWidth() * OVER_SCALE_FACTOR;
        }
        // currentScale = smallScale;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.e("TAG", "ScalableImageView2 onDraw offsetX:" + offsetX + "  offsetY=" + offsetY + " " +
                " mScaleFraction=" + mScaleFraction);
        //放大缩小重复但是不移动，offsetX为0（这个主要是为移动用的）
        // canvas.translate(offsetX , offsetY );
        //这个也是有问题的，缩到最小，offsetX不为0
        canvas.translate(offsetX * mScaleFraction, offsetY * mScaleFraction);
        if (mScaleFraction == 0) {
            offsetX = 0;
            offsetY = 0;
        }
        //这个其实直接用animator的监听器做也可以
        float scale = smallScale + (bigScale - smallScale) * mScaleFraction;
        canvas.scale(scale, scale, getWidth() / 2f, getHeight() / 2f);
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint);
    }

    private float getScaleFraction() {
        return mScaleFraction;
    }

    private void setScaleFraction(float scaleFraction) {
        this.mScaleFraction = scaleFraction;
        invalidate();
    }

    private ObjectAnimator getScaleAnimator() {
        if (scaleAnimator == null) {
            scaleAnimator = ObjectAnimator.ofFloat(this, "scaleFraction", 0, 1);
        }
        return scaleAnimator;
    }

    class HenGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent down, MotionEvent event, float distanceX,
                                float distanceY) {
            if (big) {
                offsetX -= distanceX;
                offsetY -= distanceY;
                fixOffsets();
                invalidate();
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent down, MotionEvent event, float velocityX,
                               float velocityY) {
            if (big) {
                scroller.fling((int) offsetX, (int) offsetY, (int) velocityX, (int) velocityY,
                        -(int) (bitmap.getWidth() * bigScale - getWidth()) / 2,
                        (int) (bitmap.getWidth() * bigScale - getWidth()) / 2,
                        -(int) (bitmap.getHeight() * bigScale - getHeight()) / 2,
                        (int) (bitmap.getHeight() * bigScale - getHeight()) / 2);

                postOnAnimation(henFlingRunner);
            }
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            big = !big;
            if (big) {
                getScaleAnimator().start();
            } else {
                getScaleAnimator().reverse();
            }
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }

    private void fixOffsets() {
        offsetX = Math.min(offsetX, (bitmap.getWidth() * bigScale - getWidth()) / 2);
        offsetX = Math.max(offsetX, -(bitmap.getWidth() * bigScale - getWidth()) / 2);
        offsetY = Math.min(offsetY, (bitmap.getHeight() * bigScale - getHeight()) / 2);
        offsetY = Math.max(offsetY, -(bitmap.getHeight() * bigScale - getHeight()) / 2);
    }

    class HenFlingRunner implements Runnable {

        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.getCurrX();
                offsetY = scroller.getCurrY();
                invalidate();
                postOnAnimation(this);
            }
        }
    }


}
