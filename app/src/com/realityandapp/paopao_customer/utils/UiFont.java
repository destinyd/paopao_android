package com.realityandapp.paopao_customer.utils;

import android.graphics.Typeface;
import com.realityandapp.paopao_customer.PaopaoCustomerApplication;

/**
 * Created by fushang318 on 2014/8/20.
 */
public class UiFont {
    final static public Typeface FONTAWESOME_FONT = Typeface.createFromAsset(
            PaopaoCustomerApplication.get_context().getAssets(),
            "fonts/fontawesome-webfont.ttf");
}
