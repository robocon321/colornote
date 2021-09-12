package com.example.colornote.customview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.colornote.activity.MainActivity;
import com.example.colornote.util.Constant;

public class CustomCardView extends CardView {
    private Paint paint = new Paint();
    private boolean isSelect = false;
    SharedPreferences sharedPreferences;
    String themeName;
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
        String color="#fffff";
        int line=0;
        sharedPreferences = getContext().getSharedPreferences("Theme", Context.MODE_PRIVATE);
        themeName = sharedPreferences.getString("ThemeName", "Default");
        if(themeName.equalsIgnoreCase("Dark")){
          color="#ffffff";
line=10;
        }else{
            color="#000000";
            line=6;
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(color));
        paint.setStrokeWidth(line);
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
