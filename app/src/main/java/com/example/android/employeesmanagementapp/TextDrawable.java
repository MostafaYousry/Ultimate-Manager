package com.example.android.employeesmanagementapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;

import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.utils.ColorUtils;

import androidx.core.content.res.ResourcesCompat;

public class TextDrawable extends ShapeDrawable {

    private final Paint textPaint;
    private EmployeeEntry mEmployee;
    private char mLetter;
    private int width;
    private int height;
    private int fontSize;

    public TextDrawable(Context context, EmployeeEntry employeeEntry, int width, int height, int fontSize) {

        mEmployee = employeeEntry;

        this.width = width;
        this.height = height;
        this.fontSize = fontSize;

        mLetter = Character.toUpperCase(mEmployee.getEmployeeName().charAt(0));


        int a700Color = ResourcesCompat.getColor(context.getResources(), ColorUtils.getLetterColor(mEmployee), context.getTheme());

        textPaint = new Paint();
        textPaint.setColor(a700Color);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        textPaint.setStyle(Paint.Style.FILL);
//        textPaint.setTypeface(font);
        textPaint.setTextAlign(Paint.Align.CENTER);


        int a100Color = ResourcesCompat.getColor(context.getResources(), ColorUtils.getLetterBackgroundColor(mEmployee), context.getTheme());

        Paint drawablePaint = getPaint();
        drawablePaint.setColor(a100Color);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Rect r = getBounds();


        int count = canvas.save();

        canvas.translate(r.left, r.top);

        // draw text
        int width = this.width < 0 ? r.width() : this.width;
        int height = this.height < 0 ? r.height() : this.height;
        int fontSize = this.fontSize < 0 ? (Math.min(width, height) / 2) : this.fontSize;
        textPaint.setTextSize(fontSize);


        canvas.drawText(String.valueOf(mLetter), width / 2, height / 2 - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);

        canvas.restoreToCount(count);
    }

    @Override
    public void setAlpha(int alpha) {
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        textPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }


}