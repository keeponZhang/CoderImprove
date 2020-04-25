package com.hencoder.plus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hencoder.plus.Utils;

public class SportsView extends View {
    private static final float RING_WIDTH = Utils.dp2px(2);
    private static final float RADIUS = Utils.dp2px(150);
    private static final int CIRCLE_COLOR = Color.parseColor("#90A4AE");
    private static final int HIGHLIGHT_COLOR = Color.parseColor("#FF4081");

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint linPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Rect rect = new Rect();
    Paint.FontMetrics fontMetrics = new Paint.FontMetrics();

    public SportsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        paint.setTextSize(Utils.dp2px(100));
        paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Quicksand-Regular.ttf"));
        paint.getFontMetrics(fontMetrics);
        // paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制环
        linPaint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(CIRCLE_COLOR);
        paint.setStrokeWidth(RING_WIDTH);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, RADIUS, paint);

        // 绘制进度条
        // paint.setColor(HIGHLIGHT_COLOR);
        // paint.setStrokeCap(Paint.Cap.ROUND);
        // canvas.drawArc(getWidth() / 2 - RADIUS, getHeight() / 2 - RADIUS, getWidth() / 2 + RADIUS, getHeight() / 2 + RADIUS, -90, 225, false, paint);

        // 绘制文字
        paint.setTextSize(Utils.dp2px(70));
        // paint.setStyle(Paint.Style.FILL);
        // paint.setTextAlign(Paint.Align.CENTER);
       paint.getTextBounds("blog", 0, "blog".length(), rect);

        Log.e("TAG", "SportsView onDraw:" + rect.top + "  rect.bottom=" + rect.bottom);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int offset2 =  (rect.top + rect.bottom)/2;
        Log.e("TAG", "SportsView onDraw offset2:" +offset2);
        //这里为甚要减，因为现在是偏上
        int i = centerY - offset2;

        Paint paint = new Paint();
        paint.setTextSize(160);
        Paint.FontMetricsInt fm  = paint.getFontMetricsInt();

        //这个是负数，所以需要用centerY -offset
        float offset = (fm.ascent + fm.descent) / 2;
        Log.e("TAG", "SportsView onDraw:"+fm.ascent+" fm.descent="+fm.descent );
        float baseline0 = centerY -offset;
        float baseline =
                centerY + (fm.bottom -fm.top)/2 - fm.bottom;

        Log.e("TAG", "SportsView onDraw:" +(fm.bottom- fm.descent)+"  "+(fm.ascent- fm.top));
        Log.e("TAG", "SportsView onDraw:" +(fm.descent- centerY)+"  "+(centerY- fm.ascent));
        // float baseline =
        //         centerY + (fm.bottom -fm.top)/2 - fm.bottom;
        // Log.e("TAG", "SportsView onDraw baseline0:"+baseline0+"  baseline="+baseline );

        // canvas.drawLine(centerX, centerY, centerX + RADIUS, centerY , linPaint);
        // linPaint.setColor(Color.parseColor("#0000ff"));
        canvas.drawLine(centerX, i, centerX + RADIUS, i , paint);
        canvas.drawLine(centerX, centerY, centerX + RADIUS, centerY , paint);
        canvas.drawLine(centerX, baseline, centerX + RADIUS, baseline , paint);
        canvas.drawLine(centerX, fm.bottom+baseline, centerX + RADIUS,
                fm.bottom+baseline ,
                paint);
        canvas.drawLine(centerX, fm.descent+baseline, centerX + RADIUS,
                fm.descent+baseline ,
                paint);
        linPaint.setColor(Color.parseColor("#ff0000"));
        canvas.drawLine(centerX, fm.top+baseline, centerX + RADIUS,
                fm.top+baseline ,
                paint);
        canvas.drawLine(centerX, fm.ascent+baseline, centerX + RADIUS,
                fm.ascent+baseline ,
                paint);
        canvas.drawText("blog", centerX, centerY-offset2 , paint);
        // paint.setColor(Color.parseColor("#ff0000"));
        // canvas.drawText("blog", centerX, baseline0 , paint);



        String text = "harvic\'s blog";
        int center = 200;
        int baseLineX = 0 ;

        //设置paint

        paint.setTextSize(120); //以px为单位
        paint.setTextAlign(Paint.Align.LEFT);

        //画center线
        paint.setColor(Color.BLUE);
        canvas.drawLine(baseLineX, center, 3000, center, paint);

        //计算出baseLine位置
         fm = paint.getFontMetricsInt();
        int baseLineY = center + (fm.bottom - fm.top)/2 - fm.bottom;
        Log.w("TAG", "SportsView onDraw:" +(fm.bottom- fm.descent)+"  "+(fm.ascent- fm.top));

        //画基线
        paint.setColor(Color.RED);
        canvas.drawLine(baseLineX, baseLineY, 3000, baseLineY, paint);

        //写文字
        paint.setColor(Color.GREEN);
        canvas.drawText(text, baseLineX, baseLineY, paint);

    }
}
