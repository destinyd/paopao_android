package com.realityandapp.paopao_customer.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IOrder;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.ListViewUtils;
import com.realityandapp.paopao_customer.views.adapter.OrderGoodsDataAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

/**
 * Created by dd on 14-9-18.
 */
public class OrderActivity extends PaopaoBaseActivity implements View.OnClickListener {
    private static final String FORMAT_PRICE = "￥%.2f";
    private static final String FORMAT_CONTACT = "%s(%s)";
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    @InjectView(R.id.tv_order_total)
    TextView tv_order_total;
    @InjectView(R.id.tv_delivery_price)
    TextView tv_delivery_price;
    @InjectView(R.id.tv_contact)
    TextView tv_contact;
    @InjectView(R.id.tv_address)
    TextView tv_address;
    @InjectView(R.id.tv_order_status)
    TextView tv_order_status;
    @InjectView(R.id.lv_order_goods_data)
    ListView lv_order_goods_data;
    @InjectView(R.id.fatv_destroy)
    FontAwesomeButton fabtn_destroy;
    @InjectView(R.id.fabtn_edit)
    FontAwesomeButton fatv_edit;
    @InjectView(R.id.fabtn_back)
    FontAwesomeButton fabtn_back;
    @InjectView(R.id.btn_submit)
    Button btn_submit;
    @InjectView(R.id.tv_deliveryman)
    TextView tv_deliveryman;
    @InjectView(R.id.rl_deliveryman)
    RelativeLayout rl_deliveryman;

    private IOrder order;
    private AlertDialog dialog_confirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

        setTitle("订单详情");
        get_data();
    }

    private void get_data() {
        new RoboAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                order = DataProvider.get_order("1");
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                build_views();
                loading_view.hide();
            }
        }.execute();
    }

    private void build_views() {
        build_actionbar();
        build_status();
        build_deliveryman();
        build_total();
        build_delivery();
        build_address();
        build_cart_to_order();
        build_submit();
    }

    private void build_submit() {
        btn_submit.setOnClickListener(this);
        if ("等待支付".equals(order.get_status())) {
            btn_submit.setText("支付");
            btn_submit.setVisibility(View.VISIBLE);
        } else {
            btn_submit.setVisibility(View.INVISIBLE);
        }
    }

    private void build_actionbar() {
        fatv_edit.setOnClickListener(this);
        fabtn_destroy.setOnClickListener(this);
        fabtn_back.setOnClickListener(this);
    }

    private void build_deliveryman() {
        rl_deliveryman.setOnClickListener(this);
        tv_deliveryman.setText(order.get_deliveryman().get_realname());
    }

    private void build_status() {
        tv_order_status.setText(order.get_status());
    }

    private void build_delivery() {
        tv_delivery_price.setText(String.format(FORMAT_PRICE, order.get_delivery_price()));
    }

    private void build_cart_to_order() {
        OrderGoodsDataAdapter adapter =
                new OrderGoodsDataAdapter(getLayoutInflater(), order.get_goods_data());
        lv_order_goods_data.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(lv_order_goods_data);
    }

    private void build_total() {
        tv_order_total.setText(String.format(FORMAT_PRICE, order.get_total()));
    }

    private void build_address() {
        tv_contact.setText(String.format(FORMAT_CONTACT, order.get_address().get_realname(), order.get_address().get_phone()));
        tv_address.setText(order.get_address().get_address());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabtn_edit:
                go_to_edit_order();
                break;
            case R.id.fatv_destroy:
                confirm_destroy();
                break;
            case R.id.fabtn_back:
                finish();
                break;
            case R.id.btn_submit:
                submit();
                break;
            case R.id.rl_deliveryman:
                go_to_im();
                break;
        }
    }

    private void confirm_destroy() {
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this)
                .setTitle("确定删除吗？")
                .setNeutralButton("取消", null)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        destroy_order();
                    }
                });
        dialog_confirm = dialog_builder.create();
        dialog_confirm.show();
    }

    private void go_to_im() {
        Intent intent = new Intent(this, IMActivity.class);
        intent.putExtra(Constants.Extra.DELIVERYMAN, order.get_deliveryman());
        startActivity(intent);
    }

    private void submit() {
        System.out.println("submit");
        if ("等待支付".equals(order.get_status())) {
            Intent intent = new Intent(this, PayActivity.class);
            intent.putExtra(Constants.Extra.ORDER, order);
            startActivity(intent);
        } else {
//            todo for other status
            btn_submit.setVisibility(View.INVISIBLE);
        }
    }

    private void go_to_edit_order() {
        Intent intent = new Intent(OrderActivity.this, EditOrderActivity.class);
        intent.putExtra(Constants.Extra.ORDER, order);
        startActivityForResult(intent, Constants.Request.ORDER);
    }

    private void destroy_order() {
        new RoboAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                order.destroy();
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                loading_view.hide();
                finish();
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.Request.ORDER:
                if (resultCode == RESULT_OK) {
                    order = (IOrder) data.getSerializableExtra(Constants.Extra.ORDER);
                    build_views();
                }

            break;
        }
    }
}