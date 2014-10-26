package com.realityandapp.paopao_customer.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.User;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dd on 14-6-12.
 */
public class SignUpActivity extends PaopaoBaseActivity {
    private static final java.lang.String PHONE_PATTERN = "^1\\d{10}$";
    private static final java.lang.String NAME_PATTERN = "[\\u4e00-\\u9fa5_a-zA-Z0-9_]{2,10}";
    private static final java.lang.String EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    private static final java.lang.String VERIFY_CODE_PATTERN = "\\d{4}";
    @InjectView(R.id.et_phone)
    EditText et_phone;
    @InjectView(R.id.et_verify_code)
    EditText et_verify_code;
    @InjectView(R.id.btn_get_verify_code)
    Button btn_get_verify_code;
    @InjectView(R.id.et_name)
    EditText et_name;
    @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.et_email)
    EditText et_email;
    @InjectView(R.id.btn_sign_up)
    Button btn_sign_up;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;

    int get_verify_code_resume_seconds = 0;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            btn_get_verify_code.setText(String.format(Constants.Format.VERIFY_CODE_TIMEOUT, get_verify_code_resume_seconds));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up);
        setTitle("注册");
        btn_sign_up.setOnClickListener(this);
        btn_get_verify_code.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (User.current() != null) {
            finish();
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                sign_up();
                break;
            case R.id.btn_get_verify_code:
                get_verify_code();
                break;
            default:
                super.onClick(v);
        }
    }

    private void get_verify_code() {
        final String phone = et_phone.getText().toString();
        if (!valid_phone(phone)) {
            Toast.makeText(this, "手机格式错误", Toast.LENGTH_LONG).show();
            return;
        }
        new RoboAsyncTask<Integer>(this) {
            @Override
            public Integer call() throws Exception {
                return DataProvider.get_verify_code(phone);
            }

            @Override
            protected void onPreExecute() throws Exception {
                super.onPreExecute();
                btn_get_verify_code_show_sending();
            }

            @Override
            protected void onSuccess(Integer i) throws Exception {
                super.onSuccess(i);
                if (i >= 0)
                    btn_get_verify_code_delay(i);
                else
                    alert_errors();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                alert_errors();
                btn_get_verify_code_resume();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
            }
        }.execute();
    }

    private void btn_get_verify_code_resume() {
        btn_get_verify_code.setText(R.string.get_verify_code);
        btn_get_verify_code.setEnabled(true);
    }

    private void btn_get_verify_code_show_sending() {
        btn_get_verify_code.setEnabled(false);
        btn_get_verify_code.setText(R.string.sending);
    }

    private void alert_errors() {
        Toast.makeText(this, "注册失败", Toast.LENGTH_LONG).show();
    }

    private void btn_get_verify_code_delay(int i) {
        btn_get_verify_code.setEnabled(false);
        get_verify_code_resume_seconds = i;
        new RoboAsyncTask<Void>(this){

            @Override
            public Void call() throws Exception {
                while (get_verify_code_resume_seconds > 0) {
                    Thread.sleep(1000);
                    Message message = new Message();
                    message.what = 1;
                    SignUpActivity.this.handler.sendMessage(message);// 发送消息
                    get_verify_code_resume_seconds -= 1;
                }
                btn_get_verify_code_resume();
                return null;
            }
        }.execute();
    }

    private void sign_up() {
        final String name = et_name.getText().toString();
        final String phone = et_phone.getText().toString();
        final String email = et_email.getText().toString();
        final String password = et_password.getText().toString();
        final String verify_code = et_verify_code.getText().toString();

        if (!valid_phone(phone) || !valid_name(name) || !valid_or_blank_email(email) ||
                !valid_verify_code(verify_code))
            return;

        new RoboAsyncTask<User>(this) {
            @Override
            public User call() throws Exception {
                return DataProvider.sign_up(phone, verify_code, password, name, email);
            }

            @Override
            protected void onPreExecute() throws Exception {
                super.onPreExecute();
                loading_view.show();
            }

            @Override
            protected void onSuccess(User user) throws Exception {
                super.onSuccess(user);
                if(user != null){
                    user.save();
                    finish_with_ok();
                }else {
                    alert_errors();
                }
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                alert_errors();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                loading_view.hide();
            }
        }.execute();
    }

    private boolean valid_verify_code(String verify_code) {
        Pattern pattern = Pattern.compile(VERIFY_CODE_PATTERN);
        Matcher matcher = pattern.matcher(verify_code);
        return matcher.matches();
    }

    private boolean valid_or_blank_email(String email) {
        return TextUtils.isEmpty(email) || valid_email(email);
    }

    private boolean valid_email(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean valid_name(String name) {
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private void finish_with_ok() {
        set_result_ok();
        finish();
    }

    private void set_result_ok() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }

    private boolean valid_phone(String phone) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
