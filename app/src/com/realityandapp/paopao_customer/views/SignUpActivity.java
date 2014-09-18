package com.realityandapp.paopao_customer.views;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectView;

/**
 * Created by dd on 14-6-12.
 */
public class SignUpActivity extends PaopaoBaseActivity {
//    AuthenticatorsController myAuthenticator;
//    User current_user;
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
    @InjectView(R.id.btn_signup)
    Button btn_signup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(et_phone.getText().toString());
                System.out.println(et_verify_code.getText().toString());
                System.out.println(et_name.getText().toString());
                System.out.println(et_password.getText().toString());
                System.out.println(et_email.getText().toString());
            }
        });

        btn_get_verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(et_phone.getText().toString());
            }
        });
    }
}
