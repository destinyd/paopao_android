package com.realityandapp.paopao_customer.views.base;

import android.os.Bundle;
import android.widget.TextView;
import com.realityandapp.paopao_customer.R;
import roboguice.activity.RoboFragmentActivity;

/**
 * Created by dd on 14-9-18.
 */
public class PaopaoBaseActivity extends RoboFragmentActivity {
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
}