package com.realityandapp.paopao_customer.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mindpin.android.authenticator.AuthCallback;
import com.mindpin.android.authenticator.IUser;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.controllers.AuthenticatorsController;
import com.realityandapp.paopao_customer.models.User;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectView;

/**
 * Created by dd on 14-6-12.
 */
public class SignInActivity extends PaopaoBaseActivity {
    AuthenticatorsController myAuthenticator;
    User current_user;

    //    AuthenticatorsController myAuthenticator;
//    User current_user;
    @InjectView(R.id.et_login)
    EditText et_login;
    @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.btn_signin)
    Button btn_signin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in);
        setTitle("登录");
        myAuthenticator = new AuthenticatorsController(this);
//        current_user = User.current();
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(et_login.getText().toString());
                System.out.println(et_password.getText().toString());
                // 关闭软键盘
                // 如果不关闭软键盘会触发下个页面的抽屉导航BUG，从而使页面显示不正常
                // 先用关闭软键盘的方式避免触发抽屉导航BUG
                InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                }

                btn_signin.setEnabled(false);
                System.out.println("get_sign_in_url():" + myAuthenticator.get_sign_in_url());
                myAuthenticator.sign_in(
                        et_login.getText().toString(),
                        et_password.getText().toString(),
                        new AuthCallback() {
                            @Override
                            public void success(IUser user) {
                                SignInActivity.this.finish();
                                startActivity(new Intent(SignInActivity.this, RealMainActivity.class));
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
            }
        });
    }
}
