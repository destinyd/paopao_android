package com.realityandapp.paopao_customer.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IAddress;
import com.realityandapp.paopao_customer.models.test.Address;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.PaopaoAsyncTask;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

/**
 * Created by dd on 14-9-24.
 */
public class NewAddressActivity extends PaopaoBaseActivity {
    @InjectView(R.id.fatv_save)
    FontAwesomeButton fatv_save;
    @InjectView(R.id.et_address)
    EditText et_address;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;

    private IAddress address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_address);
        bind_views();
        address = new Address();
        et_address.setText(address.get_address());
    }

    private void bind_views() {
        fatv_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fatv_save:
                create_address_and_return();
                break;
            default:
                super.onClick(view);
        }
    }

    private void create_address_and_return() {
        create_address();
    }

    private void create_address() {
        new PaopaoAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                address.save();
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                return_address();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                loading_view.hide();
            }
        }.execute();
    }

    private void return_address() {
        Intent intent = new Intent();
        intent.putExtra(Constants.Extra.ADDRESS, address);
        setResult(RESULT_OK, intent);
        finish();
    }
}