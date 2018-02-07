package com.ckj.dotaguide.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.ckj.dotaguide.R;

/**
 * Created by chenkaijian on 17-9-19.
 */
public class RotateTextView extends AppCompatTextView {

    private int degree;

    public RotateTextView(Context context) {
        super(context);
    }

    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RotateTextView);
        degree = ta.getInteger(R.styleable.RotateTextView_degree, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(degree, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        super.onDraw(canvas);
    }

}