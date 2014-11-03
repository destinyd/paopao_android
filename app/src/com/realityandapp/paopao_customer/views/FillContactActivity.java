package com.realityandapp.paopao_customer.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IAddress;
import com.realityandapp.paopao_customer.models.http.Address;
import com.realityandapp.paopao_customer.utils.PaopaoAsyncTask;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dd on 14-6-12.
 */
public class FillContactActivity extends PaopaoBaseActivity {
    private static final java.lang.String PHONE_PATTERN = "^1\\d{10}$";
    private static final java.lang.String REALNAME_PATTERN = "^([\\u4e00-\\u9fa5]+|([a-zA-Z]+\\s?)+)$";
    @InjectExtra(Constants.Extra.ADDRESS)
    private Address address;

    @InjectView(R.id.fa_btn_ok)
    FontAwesomeButton fa_btn_save;
    @InjectView(R.id.et_address)
    TextView et_address;
    @InjectView(R.id.et_plus)
    EditText et_plus;
    @InjectView(R.id.et_realname)
    EditText et_realname;
    @InjectView(R.id.et_phone)
    EditText et_phone;

    @InjectView(R.id.loading_view)
    LoadingView loading_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fill_contact);
        init();
    }

    private void init() {
        setTitle("新建地址-第二步");
        et_address.setText(address.get_address());
        bind_views();
    }

    private void bind_views() {
        fa_btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fa_btn_ok:
                save_and_return_result_ok();
                break;
            default:
                super.onClick(view);
        }
    }

    private void save_and_return_result_ok() {
        address.set_phone(et_phone.getText().toString());
        address.set_plus(et_plus.getText().toString());
        address.set_realname(et_realname.getText().toString());
        if(validate_address()) {
            new PaopaoAsyncTask<Void>(this) {

                @Override
                protected void onPreExecute() throws Exception {
                    loading_view.show();
                }

                @Override
                public Void call() throws Exception {
                    IAddress tmp = address.save();
                    if(tmp != null)
                        address = (Address) tmp;
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

    }

    private boolean validate_address() {
        boolean is_valid = true;
        if(!is_valid_phone(et_phone.getText().toString())) {
            is_valid = false;
            Toast.makeText(this, "手机号码填写不正确", Toast.LENGTH_LONG).show();
        }
        if(!is_valid_realname(et_realname.getText().toString())) {
            is_valid = false;
            Toast.makeText(this, "姓名填写不正确", Toast.LENGTH_LONG).show();
        }
        return is_valid;
    }

    private boolean is_valid_realname(String realname) {
        Pattern pattern = Pattern.compile(REALNAME_PATTERN);
        Matcher matcher = pattern.matcher(realname);
        return matcher.matches();
    }

    // validating email id
    private boolean is_valid_phone(String phone) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private void return_address() {
        Intent intent = new Intent();
        intent.putExtra(Constants.Extra.ADDRESS, address);
        setResult(RESULT_OK, intent);
        finish();
    }
}
