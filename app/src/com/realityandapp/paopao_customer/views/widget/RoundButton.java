package com.realityandapp.paopao_customer.views.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by dd on 14-9-29.
 */
public class RoundButton extends Button {
    final int color_round;
    public RoundButton(Context context) {
        super(context);
        color_round = getCurrentTextColor();
    }

    public RoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        color_round = getCurrentTextColor();
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        color_round = getCurrentTextColor();
    }
}
