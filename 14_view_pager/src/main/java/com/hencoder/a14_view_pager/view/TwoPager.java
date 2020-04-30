package com.hencoder.a14_view_pager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

public class TwoPager extends ViewGroup {
    float downX;
    float downY;
    float downScrollX;
    boolean scrolling;
    float minVelocity;
    float maxVelocity;
    OverScroller overScroller;
    ViewConfiguration viewConfiguration;
    VelocityTracker velocityTracker = VelocityTracker.obtain();

    public TwoPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        overScroller = new OverScroller(context);
        viewConfiguration = ViewConfiguration.get(context);
        maxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        minVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        int childTop = 0;
        int childRight = getWidth();
        int childBottom = getHeight();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.layout(childLeft, childTop, childRight, childBottom);
            childLeft += getWidth();
            childRight += getWidth();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        velocityTracker.addMovement(ev);

        Log.e("TAG", "TwoPager onInterceptTouchEvent:");
        boolean result = false;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                scrolling = false;
                downX = ev.getX();
                downY = ev.getY();
                downScrollX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w("TAG", "TwoPager onInterceptTouchEvent:");
                float dx = downX - ev.getX();
                if (!scrolling) {
                    if (Math.abs(dx) > viewConfiguration.getScaledPagingTouchSlop()) {
                        scrolling = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                        result = true;
                    }
                }
                break;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", "TwoPager onTouchEvent:");
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        velocityTracker.addMovement(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                downScrollX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                //依然是上一个点减去当前的点(手指向左，其实向右移动，dx>0),所以这里可以直接加downScrollX
                float dx = downX - event.getX() + downScrollX;
                Log.e("TAG",
                        "TwoPager onTouchEvent dx:"+dx+"  (downX - event.getX())"+(downX - event.getX()));
                if (dx > getWidth()) {
                    dx = getWidth();
                } else if (dx < 0) {
                    dx = 0;
                }
                //子view比父View大适用,正值是往左，dx（scrollx）是子view左上角为原点，然后父view离子view左上角的值
                scrollTo((int) (dx), 0);
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                float vx = velocityTracker.getXVelocity();
                int scrollX = getScrollX();
                int targetPage;
                if (Math.abs(vx) < minVelocity) {
                    targetPage = scrollX > getWidth() / 2 ? 1 : 0;
                } else {
                    //速度大看速度朝向
                    targetPage = vx < 0 ? 1 : 0;
                }
                int scrollDistance = targetPage == 1 ? (getWidth() - scrollX) : - scrollX;
                overScroller.startScroll(getScrollX(), 0, scrollDistance, 0);
                postInvalidateOnAnimation();
                break;
        }
        //这里表示消费
        return true;
    }

    @Override
    public void computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            postInvalidateOnAnimation();
        }
    }
    public  String getActioString(MotionEvent event){
        int action = event.getAction();
        if(action ==MotionEvent.ACTION_DOWN){
            return "ACTION_DOWN";
        } else if(action ==MotionEvent.ACTION_MOVE){
            return "ACTION_MOVE";
        }else if(action ==MotionEvent.ACTION_UP){
            return "ACTION_UP";
        }else if(action ==MotionEvent.ACTION_CANCEL){
            return "ACTION_CANCEL";
        }
        return "";
    }
}
