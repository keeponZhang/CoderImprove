package com.hencoder.plus.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.plus.R;
import com.hencoder.plus.Utils;

public class AvatarView extends View {
    private static final float WIDTH = Utils.dp2px(300);
    private static final float PADDING = Utils.dp2px(20);
    private static final float EDGE_WIDTH = Utils.dp2px(10);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //后画的是src ，src_In与透明有关
    Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    Bitmap bitmap;
    RectF savedArea = new RectF();

    public AvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        bitmap = getAvatar((int) WIDTH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // savedArea.set(PADDING, PADDING, PADDING + WIDTH, PADDING + WIDTH);
        //这里大一点也没事
        savedArea.set(PADDING, PADDING, PADDING + WIDTH+500, PADDING + WIDTH+500);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //这里主要会加个黑框
        paint.setColor(getResources().getColor(android.R.color.holo_green_dark));
        canvas.drawOval(PADDING, PADDING, PADDING + WIDTH, PADDING + WIDTH, paint);
        //这个方法会生成透明画布
        int saved = canvas.saveLayer(savedArea, paint);
        // canvas.save();
        paint.setColor(getResources().getColor(android.R.color.holo_red_dark));
        canvas.drawOval(PADDING + EDGE_WIDTH, PADDING + EDGE_WIDTH, PADDING + WIDTH - EDGE_WIDTH, PADDING + WIDTH - EDGE_WIDTH, paint);
        paint.setXfermode(xfermode);
        canvas.drawBitmap(bitmap, PADDING, PADDING, paint);
        paint.setXfermode(null);
        // canvas.restore();
        canvas.restoreToCount(saved);
    }

    Bitmap getAvatar(int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.avatar_rengwuxian, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(getResources(), R.drawable.avatar_rengwuxian, options);
    }
}
