package com.example.colornote.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class CustomCardView extends CardView {
    private Paint paint = new Paint();
    private boolean isSelect = false;

    public CustomCardView(@NonNull Context context) {
        super(context);
        setBorder();
    }

    public CustomCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBorder();
    }

    public CustomCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBorder();
    }


    public void setBorder(){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#000000"));
        paint.setStrokeWidth(4);
    }

    public void addBorder(){
        isSelect = true;
        invalidate();
    }

    public void removeBorder(){
        isSelect = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isSelect){
            canvas.drawLine(0, 0, getWidth(), 0, paint);
            canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), paint);
            canvas.drawLine(getWidth(), getHeight(), 0, getHeight(), paint);
            canvas.drawLine(0, getHeight(), 0,0, paint);
        }
    }

}
