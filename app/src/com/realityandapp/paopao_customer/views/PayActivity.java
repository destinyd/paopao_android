package com.realityandapp.paopao_customer.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectView;

/**
 * Created by dd on 14-6-12.
 */
public class PayActivity extends PaopaoBaseActivity {
    @InjectView(R.id.btn_pay)
    Button btn_pay;
    private AlertDialog alert_dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pay);
        setTitle("支付");

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("pay");
                AlertDialog.Builder dialog_builder = new AlertDialog.Builder(PayActivity.this)
                        .setTitle("支付")
                        .setMessage("请根据支付情况选择相应选项")
                        .setNegativeButton("支付成功", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish_with_result();
                            }
                        })
                        .setNeutralButton("遇到问题？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                repay();
                            }
                        });
                alert_dialog = dialog_builder.create();
                alert_dialog.show();
            }
        });
    }

    private void finish_with_result() {
        System.out.println("finish_with_result");
        Intent intent = new Intent(this, OrderActivity.class);
//        intent.putExtra(Constants.Extra.ORDER, order);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void repay() {
        // todo only dismiss? or goto wiki
        System.out.println("repay");
        alert_dialog.dismiss();
    }
}
