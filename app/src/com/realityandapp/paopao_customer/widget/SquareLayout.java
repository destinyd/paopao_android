package com.realityandapp.paopao_customer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;


public class SquareLayout extends RelativeLayout {
    public Context context;
    public static int height;

    public SquareLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SquareLayout(Context context) {
        super(context);
        this.context = context;
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        int child_width = getMeasuredWidth();
//        int child_height = getMeasuredHeight();


//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int child_width = (metrics.widthPixels - 5 * 3) / 2;



        height = child_width;

//        Log.i("child_width 大小 ", Integer.toString(child_width));
        // Log.i("child_height 大小 ", Integer.toString(child_height));

        heightMeasureSpec = widthMeasureSpec =
                MeasureSpec.makeMeasureSpec(child_width, MeasureSpec.EXACTLY);

//        Log.i("widthMeasureSpec 大小 ", Integer.toString(widthMeasureSpec));
//        Log.i("heightMeasureSpec 大小 ", Integer.toString(heightMeasureSpec));

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}

