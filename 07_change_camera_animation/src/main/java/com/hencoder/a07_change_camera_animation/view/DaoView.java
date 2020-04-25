package com.hencoder.a07_change_camera_animation.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.a07_change_camera_animation.Utils;

import androidx.annotation.Nullable;

/**
 * createBy	 keepon
 */
public class DaoView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DaoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DaoView(Context context,
                   @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int imageWidth = 400;
        //二维的没问题
        // canvas.translate(200,100);
        // canvas.drawBitmap(Utils.getAvatar(getResources(), imageWidth), 0, 0, paint);


        //不需要两个坐标系
        // canvas.rotate(45,imageWidth/2,imageWidth/2);
        // canvas.drawBitmap(Utils.getAvatar(getResources(), imageWidth), 0, 0, paint);

        canvas.save();
        canvas.translate(100, 100);
        canvas.drawColor(Color.BLACK);
        canvas.rotate(45,imageWidth/2,imageWidth/2);
        //这个是不对的，移动canvas应该以新的canvas
        // canvas.rotate(45,200+imageWidth/2,200+imageWidth/2);
        canvas.drawBitmap(Utils.getAvatar(getResources(), imageWidth), 0, 0, paint);
        canvas.restore();

        // 也可以反过来写（先旋转，再移动，再绘制不过好像对不上）
        canvas.rotate(45,0,0);
        canvas.translate(-100, -100);
        canvas.drawBitmap(Utils.getAvatar(getResources(), imageWidth), 0, 0, paint);
    }
}
