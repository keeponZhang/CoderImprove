package com.hencoder.a10_layout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hencoder.a10_layout.Utils;

public class CircleView extends View {
    private static final int RADIUS = (int) Utils.dpToPixel(80);
    private static final int PADDING = (int) Utils.dpToPixel(30);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (PADDING + RADIUS) * 2;
        int height = (PADDING + RADIUS) * 2;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int heightsize = MeasureSpec.getSize(heightMeasureSpec);
        Log.w("TAG",
                "CircleView onMeasure size:" +size+"  heightsize="+heightsize+" "+Utils.dpToPixel(200));

        //resolveSize
        width = resolveSizeAndState(width, widthMeasureSpec, 0);
        height = resolveSizeAndState(height, heightMeasureSpec, 0);
        Log.e("TAG", "CircleView onMeasure width:" +width+"  height="+height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.RED);
        canvas.drawCircle(PADDING + RADIUS, PADDING + RADIUS, RADIUS, paint);
    }
}
