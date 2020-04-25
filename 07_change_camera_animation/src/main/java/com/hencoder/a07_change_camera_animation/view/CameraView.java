package com.hencoder.a07_change_camera_animation.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.a07_change_camera_animation.Utils;

public class CameraView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Camera camera = new Camera();

    public CameraView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        camera.rotateX(45);
        camera.setLocation(0, 0, Utils.getZForCamera()); // -8 = -8 * 72
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制上半部分
        canvas.save();
        canvas.translate(100 + 600 / 2, 100 + 600 / 2);
        canvas.rotate(-20);
        //这里表示宽度1200了，高度600，但是时再上部分的，屏幕看不到，但是有第一行的translate
        //这时图片中心是到原点了，
        //这个更好理解，虽然下面的600也能达到效果，但是旋转后就需要更大
        // canvas.clipRect(- 300, - 300, 300, 0);
        canvas.clipRect(- 600, - 600, 600, 0);
        canvas.drawColor(Color.BLACK);
        canvas.rotate(20);
        //这里也很迷，向左移动canvas，不是更看不到了吗，所以需要第一行的translate,第一行translate了为甚么也看不到
        canvas.translate(- (100 + 600 / 2), - (100 + 600 / 2));
        //这个是以画布为对照的
        canvas.drawBitmap(Utils.getAvatar(getResources(), 600), 100, 100, paint);
        canvas.restore();
        paint.setColor(Color.BLUE);
        canvas.drawLine(0,400,getMeasuredWidth(),400,paint);
        canvas.drawLine(1000,0,1000,getMeasuredHeight(),paint);
        // 绘制下半部分
        canvas.save();
        //做三维变换之前，就去切割，
        canvas.translate(100 + 600 / 2, 100 + 600 / 2);
        canvas.rotate(-20);
        camera.applyToCanvas(canvas);
        // canvas.clipRect(- 300, 0, 300, 300);
        canvas.clipRect(- 600, 0, 600, 600);
        canvas.rotate(20);
        canvas.translate(- (100 + 600 / 2), - (100 + 600 / 2));
        canvas.drawBitmap(Utils.getAvatar(getResources(), 600), 100, 100, paint);
        canvas.restore();



    }
}
