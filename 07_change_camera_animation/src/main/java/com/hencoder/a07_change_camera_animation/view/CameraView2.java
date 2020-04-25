package com.hencoder.a07_change_camera_animation.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.a07_change_camera_animation.Utils;

import androidx.annotation.Nullable;

public class CameraView2 extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Camera camera = new Camera();

    public CameraView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        camera.rotateX(45);
        // camera.setLocation(0, 0, -8); // -8 = -8 * 72
        camera.setLocation(0, 0, Utils.getZForCamera());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制上半部分
        // canvas.translate(-300, -300);
        // camera.applyToCanvas(canvas);
        // canvas.translate(300, 300);
        // canvas.drawBitmap(Utils.getAvatar(getResources(), 400), 100, 100, paint);

        //切割一半,这为甚是 getWidth()，说明画在300之前的是无效的
        canvas.clipRect(0, 300, getWidth(), getHeight());
        canvas.translate(300, 300);
        camera.applyToCanvas(canvas);
        canvas.translate(-300, -300);
        canvas.drawBitmap(Utils.getAvatar(getResources(), 400), 100, 100, paint);




        //先移到原点0,0位置clipRect应该是left应该是-300，right应该是300（凯哥推荐这种，三维变换之前就去切割，这里是反序看，所以这里切割是在前面）
        //感觉二维里用相对的，三维里用凯哥推荐的
        // canvas.translate(300, 300);//移回去
        // camera.applyToCanvas(canvas);//做三维变换
        // canvas.clipRect(-200, 0, 200, 200);//然后切割
        // canvas.translate(-300, -300);//然后移动到原点
        // canvas.drawBitmap(Utils.getAvatar(getResources(), 400), 100, 100, paint);//第一行绘制

    }
}
