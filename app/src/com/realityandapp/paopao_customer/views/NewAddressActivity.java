package com.realityandapp.paopao_customer.views;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.inject.InjectView;

/**
 * Created by dd on 14-9-24.
 */
public class NewAddressActivity extends PaopaoBaseActivity implements View.OnClickListener {
    @InjectView(R.id.fatv_save)
    FontAwesomeButton fatv_save;
    @InjectView(R.id.et_address)
    EditText et_address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_address);
        bind_views();
    }

    private void bind_views() {
        fatv_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fatv_save:
                //todo address save
                System.out.println("fatv save");
                break;
        }
    }
}