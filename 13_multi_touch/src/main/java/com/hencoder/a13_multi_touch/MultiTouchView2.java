package com.hencoder.a13_multi_touch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MultiTouchView2 extends View {
    private static final float IMAGE_WIDTH = Utils.dpToPixel(200);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;

    float downX;
    float downY;
    float offsetX;
    float offsetY;
    float originalOffsetX;
    float originalOffsetY;

    public MultiTouchView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        bitmap = Utils.getAvatar(getResources(), (int) IMAGE_WIDTH);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float sumX = 0;
        float sumY = 0;
        int pointerCount = event.getPointerCount();
        boolean isPointerUp = event.getActionMasked() == MotionEvent.ACTION_POINTER_UP;
        //抬起一瞬间是ACTION_POINTER_UP事件，但是那个点还没消失，记下的依然是两个焦点，但是下一个Move是一个焦点来算的
        for (int i = 0; i < pointerCount; i++) {
            //并且是抬起的那个手指（抬起的不一定是最后一个手指）
            if (!(isPointerUp && i == event.getActionIndex())) {
                sumX += event.getX(i);
                sumY += event.getY(i);
            }
        }
        if (isPointerUp) {
            pointerCount -= 1;
        }
        float focusX = sumX / pointerCount;
        float focusY = sumY / pointerCount;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                downX = focusX;
                downY = focusY;
                originalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = originalOffsetX + focusX - downX;
                offsetY = originalOffsetY + focusY - downY;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }
}
