package com.realityandapp.paopao_customer.views.base;

import android.os.Bundle;
import android.widget.TextView;
import com.readystatesoftware.viewbadger.BadgeView;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.views.widget.FontAwesomeTextView;
import roboguice.activity.RoboFragmentActivity;

/**
 * Created by dd on 14-9-18.
 */
public class PaopaoBaseActivity extends RoboFragmentActivity {
    private FontAwesomeTextView fatv_cart = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setTitle(CharSequence title) {
        ((TextView)findViewById(R.id.title)).setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        ((TextView)findViewById(R.id.title)).setText(titleId);
    }

    protected void set_cart_count(int count){
        if(fatv_cart == null)
            fatv_cart = (FontAwesomeTextView) findViewById(R.id.fatv_cart);
        if(fatv_cart != null && count >= 0) {
            BadgeView badge = new BadgeView(this, fatv_cart);
            badge.setTextSize(getResources().getDimensionPixelSize(R.dimen.cart_badge_text_size));
            if(count > 99)
                badge.setText("99+");
            else
                badge.setText(String.valueOf(count));
            badge.show();
        }
    }
}