package com.realityandapp.paopao_customer.views.base;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.readystatesoftware.viewbadger.BadgeView;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.activity.RoboFragmentActivity;

/**
 * Created by dd on 14-9-18.
 */
public class PaopaoBaseActivity extends RoboFragmentActivity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bind_click(R.id.fabtn_back);
    }

    private void bind_click(int id) {
        System.out.println("bind fabtn back");
        View view = findViewById(id);
        if(view != null)
            view.setOnClickListener(this);
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
        FontAwesomeButton fatv_cart = (FontAwesomeButton) findViewById(R.id.fabtn_cart);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabtn_back:
                finish();
                break;
        }
    }
}