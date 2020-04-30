package com.hencoder.a13_multi_touch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MultiTouchView1 extends View {
    private static final float IMAGE_WIDTH = Utils.dpToPixel(200);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;

    float downX;
    float downY;
    float offsetX;
    float offsetY;
    float originalOffsetX;
    float originalOffsetY;
    int trackingPointerId;

    public MultiTouchView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        bitmap = Utils.getAvatar(getResources(), (int) IMAGE_WIDTH);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                trackingPointerId = event.getPointerId(0);
                Log.w("TAG", "onTouchEvent ACTION_DOWN:");
                //需要记下初始值
                downX = event.getX();
                downY = event.getY();
                originalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                //根据id拿index
                int index = event.findPointerIndex(trackingPointerId);
                //初始偏移和手机移动导致的偏移
                offsetX = originalOffsetX + event.getX(index) - downX;
                offsetY = originalOffsetY + event.getY(index) - downY;
                Log.e("TAG", "MultiTouchView1 onTouchEvent ACTION_MOVE offsetX:"+offsetX+" " +
                        "offsetY="+offsetY);
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int actionIndex = event.getActionIndex();
                trackingPointerId = event.getPointerId(actionIndex);
                Log.d("TAG", "MultiTouchView1 onTouchEvent ACTION_POINTER_DOWN " +
                        "trackingPointerId:"+trackingPointerId);
                downX = event.getX(actionIndex);
                downY = event.getY(actionIndex);
                originalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                actionIndex = event.getActionIndex();
                int pointerId = event.getPointerId(actionIndex);
                Log.e("TAG", "MultiTouchView1 onTouchEvent pointerId:"+pointerId+"  " +
                        "trackingPointerId="+trackingPointerId);
                //活跃的抬起了
                if (pointerId == trackingPointerId) {
                    Log.w("TAG", "MultiTouchView1 onTouchEvent pointerId == trackingPointerId " +
                            "actionIndex:"+actionIndex);
                    int newIndex;
                    //如果抬起是最大的
                    if (actionIndex == event.getPointerCount() - 1) {
                        newIndex = event.getPointerCount() - 2;
                    } else {
                        newIndex = event.getPointerCount() - 1;
                    }
                    trackingPointerId = event.getPointerId(newIndex);
                    downX = event.getX(actionIndex);
                    downY = event.getY(actionIndex);
                    originalOffsetX = offsetX;
                    originalOffsetY = offsetY;
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }
}
