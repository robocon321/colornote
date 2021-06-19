package com.example.colornote.viewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class CustomViewEmpty extends View {
    private Paint paint = new Paint();
    private boolean isSelected = false;
    public CustomViewEmpty(Context context) {
        super(context);
        setBorder();
    }

    public CustomViewEmpty(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBorder();
    }

    public CustomViewEmpty(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBorder();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomViewEmpty(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setBorder();
    }

    public void setBorder(){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#000000"));
        paint.setStrokeWidth(4);
    }

    public void addBorder(){
        isSelected = true;
        invalidate();
    }

    public void removeBorder(){
        isSelected = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isSelected){
            canvas.drawLine(0, 0, getWidth(), 0, paint);
            canvas.drawLine(getWidth(), getHeight(), 0, getHeight(), paint);
            canvas.drawLine(0, getHeight(), 0,0, paint);
        }
    }
}
