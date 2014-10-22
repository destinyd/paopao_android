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
import com.realityandapp.paopao_customer.models.http.Order;
import com.realityandapp.paopao_customer.models.interfaces.IOrder;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.ListViewUtils;
import com.realityandapp.paopao_customer.utils.PaopaoAsyncTask;
import com.realityandapp.paopao_customer.views.adapter.OrderGoodsDataAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import com.realityandapp.paopao_customer.views.im.ChatActivity;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

/**
 * Created by dd on 14-9-18.
 */
public class OrderActivity extends PaopaoBaseActivity implements View.OnClickListener {
    @InjectExtra(Constants.Extra.ORDER_ID)
    public String order_id;

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

    private AlertDialog dialog_confirm;

    private IOrder order;
    private boolean need_show_qrcode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

        need_show_qrcode = getIntent().getBooleanExtra(Constants.Extra.SHOW_QRCORD, false);
        setTitle("订单详情");
        get_data();
    }

    private void get_data() {
        new PaopaoAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                order = DataProvider.my_order(order_id);
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                build_views();
                if(need_show_qrcode)
                    show_qrcode();
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
        if (order.get_status() == Order.OrderStatus.pending) {
            btn_submit.setText("支付");
            btn_submit.setVisibility(View.VISIBLE);
        } else if (order.get_status() == Order.OrderStatus.took_away) {
            btn_submit.setText("收货");
            btn_submit.setVisibility(View.VISIBLE);
        } else {
            btn_submit.setVisibility(View.INVISIBLE);
        }
    }

    private void build_actionbar() {
        fatv_edit.setOnClickListener(this);
        fabtn_destroy.setOnClickListener(this);
        fabtn_back.setOnClickListener(this);
        boolean show = order.get_status() == Order.OrderStatus.pending;
        fatv_edit.setVisibility(show ? View.VISIBLE : View.GONE);
        fabtn_destroy.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void build_deliveryman() {
        rl_deliveryman.setOnClickListener(this);
        System.out.println("order.get_deliveryman():" + order.get_deliveryman());
        if (order.get_deliveryman() == null) {
            ((View) tv_deliveryman.getParent()).setVisibility(View.GONE);
        } else {
            tv_deliveryman.setText(order.get_deliveryman().get_realname());
            ((View) tv_deliveryman.getParent()).setVisibility(View.VISIBLE);
        }
    }

    private void build_status() {
        tv_order_status.setText(order.get_str_status());
    }

    private void build_delivery() {
        tv_delivery_price.setText(String.format(Constants.Format.PRICE, order.get_delivery_price()));
    }

    private void build_cart_to_order() {
        OrderGoodsDataAdapter adapter =
                new OrderGoodsDataAdapter(getLayoutInflater(), order.get_order_items());
        lv_order_goods_data.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(lv_order_goods_data);
    }

    private void build_total() {
        tv_order_total.setText(String.format(Constants.Format.PRICE, order.get_total()));
    }

    private void build_address() {
        if (order.get_address() == null) {
            ((View)tv_address.getParent()).setVisibility(View.GONE);
        } else {
            tv_contact.setText(String.format(Constants.Format.CONTACT, order.get_address().get_realname(), order.get_address().get_phone()));
            tv_address.setText(order.get_address().get_address());
            ((View)tv_address.getParent()).setVisibility(View.VISIBLE);
        }
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
        fabtn_destroy.setEnabled(false);
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this)
                .setTitle("确定删除吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fabtn_destroy.setEnabled(true);
                    }
                })
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        destroy_order();
                    }
                })
                ;
        dialog_confirm = dialog_builder.create();
        dialog_confirm.show();
    }

    private void go_to_im() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("userId", order.get_deliveryman().get_realname());
        startActivity(intent);
    }

    private void submit() {
        System.out.println("submit");
        if (order.get_status() == Order.OrderStatus.pending) {
            Intent intent = new Intent(this, PayActivity.class);
            intent.putExtra(Constants.Extra.ORDER, order);
            startActivity(intent);
        } else if (Order.OrderStatus.took_away == order.get_status()) {
            show_qrcode();
        } else {
        }
    }

    private void show_qrcode() {
        // todo show qr code for finish
        System.out.println("show_qrcode");
    }

    private void go_to_edit_order() {
        Intent intent = new Intent(OrderActivity.this, EditOrderActivity.class);
        intent.putExtra(Constants.Extra.ORDER_ID, order.get_id());
        startActivityForResult(intent, Constants.Request.ORDER);
    }

    private void destroy_order() {
        new PaopaoAsyncTask<Void>(this) {

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
                finish_with_ok();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                loading_view.hide();
            }
        }.execute();
    }

    private void finish_with_ok() {
        set_result_ok();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.Request.ORDER:
                if (resultCode == RESULT_OK) {
                    set_result_ok();
                    get_data();
                }

                break;
        }
    }

    private void set_result_ok() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }
}