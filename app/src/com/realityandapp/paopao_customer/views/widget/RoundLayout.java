package com.realityandapp.paopao_customer.views.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import com.realityandapp.paopao_customer.R;

/**
 * Created by dd on 14-9-10.
 */


public class RoundLayout extends LinearLayout
{
    private static final float DEFAULT_ROUND_WIDTH = 2f;
    private static final int DEFAULT_ROUND_COLOR = Color.GRAY;
    private static final String TAG = "RoundLayout";

    private Path round_path;
    private int round_color = DEFAULT_ROUND_COLOR;
    private float round_width = DEFAULT_ROUND_WIDTH;
    private Paint round_path_paint;

    public RoundLayout(Context context)
    {
        super(context);
        initPaint();
    }

    public RoundLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initAttributes(context, attrs);
        initPaint();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RoundLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttributes(context, attrs);
        initPaint();
    }

    @Override
    protected void onLayout (boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        this.round_path = calculateBubblePath(l, t, r, b);
    }

    @Override
    protected void onDraw (Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawPath(round_path, round_path_paint);
    }

    private void initAttributes(Context context, AttributeSet attrs)
    {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundLayout);
        this.round_color = a.getColor(R.styleable.RoundLayout_round_color, DEFAULT_ROUND_COLOR);
        this.round_width = a.getDimension(R.styleable.RoundLayout_round_width, DEFAULT_ROUND_WIDTH);
        a.recycle();
    }

    private void initPaint()
    {
        // Setup Paint for filling the path.
        this.round_path_paint = new Paint();
//        round_path_paint.setStyle(Paint.Style.STROKE);
        round_path_paint.setStyle(Paint.Style.FILL);
//        round_path_paint.setColor(round_color);
        round_path_paint.setColor(0xff00ff00);
        Log.d(TAG, "round_color:" + round_color);
        Log.d(TAG, "round_width:" + round_width);
//        round_path_paint.setStrokeWidth(round_width);
//        round_path_paint.setShadowLayer(shadowRadius, shadowOffsetX, shadowOffsetY, shadowColor);
    }

    private Path calculateBubblePath(int l, int t, int r, int b)
    {
        Path path = new Path();

        //todo calculate padding?
        float viewHeight = b - t - getPaddingTop() - getPaddingBottom();
        float viewWidth = r - l - getPaddingLeft() - getPaddingRight();
        float radius = viewHeight > viewWidth ? viewWidth / 2f : viewHeight / 2f;
        Log.d(TAG, "viewWidth:" + viewWidth);
        Log.d(TAG, "viewHeight:" + viewHeight);
        Log.d(TAG, "radius:" + radius);
        float viewCenterX = viewWidth / 2.0f;
        float viewCenterY = viewHeight / 2.0f;
        Log.d(TAG, "viewCenterX:" + viewCenterX);
        Log.d(TAG, "viewCenterY:" + viewCenterY);
        path.addCircle(viewCenterX, viewCenterY, radius, Path.Direction.CW);

        return path;
    }
}