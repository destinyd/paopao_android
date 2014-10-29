package com.realityandapp.paopao_customer.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mindpin.android.authenticator.AuthCallback;
import com.mindpin.android.authenticator.IUser;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.PaopaoCustomerApplication;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.controllers.AuthenticatorsController;
import com.realityandapp.paopao_customer.models.User;
import com.realityandapp.paopao_customer.utils.PaopaoAsyncTask;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectView;

/**
 * Created by dd on 14-6-12.
 */
public class SignInActivity extends PaopaoBaseActivity {
    AuthenticatorsController myAuthenticator;
    @InjectView(R.id.et_login)
    EditText et_login;
    @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.btn_signin)
    Button btn_signin;
    @InjectView(R.id.btn_sign_up)
    Button btn_sign_up;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            PaopaoCustomerApplication.getInstance().im_login();
            finish_and_go_to_main();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        setTitle("登录");
        myAuthenticator = new AuthenticatorsController(this);
        btn_signin.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
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
            case R.id.btn_signin:
                // 关闭软键盘
                // 如果不关闭软键盘会触发下个页面的抽屉导航BUG，从而使页面显示不正常
                // 先用关闭软键盘的方式避免触发抽屉导航BUG
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                }

                btn_signin.setEnabled(false);
                myAuthenticator.sign_in(
                        et_login.getText().toString(),
                        et_password.getText().toString(),
                        new AuthCallback() {
                            @Override
                            public void success(IUser user) {
                                Message message = Message.obtain();
                                message.what = 1;
                                SignInActivity.this.handler.sendMessage(message);
                            }

                            @Override
                            public void failure() {
                                Toast.makeText(SignInActivity.this, "用户和密码不正确", Toast.LENGTH_LONG).show();
                                btn_signin.setEnabled(true);
                            }

                            @Override
                            public void error() {
                                Toast.makeText(SignInActivity.this, "连接服务器出错", Toast.LENGTH_LONG).show();
                                btn_signin.setEnabled(true);
                            }
                        });
                break;
            case R.id.btn_sign_up:
                go_to_sign_up();
                break;
            default:
                super.onClick(v);
        }
    }


    private void finish_and_go_to_main() {
        this.finish();
        startActivity(new Intent(this, RealMainActivity.class));
    }

    private void go_to_sign_up() {
        startActivityForResult(new Intent(this, SignUpActivity.class), Constants.Request.USER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.Request.USER:
                if (resultCode == RESULT_OK) {
                    PaopaoCustomerApplication.getInstance().im_login();
                    finish_and_go_to_main();
                }
                break;
        }
    }
}
