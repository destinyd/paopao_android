package com.realityandapp.paopao_customer.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IOrder;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.ListViewUtils;
import com.realityandapp.paopao_customer.views.adapter.OrderGoodsDataAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import com.realityandapp.paopao_customer.views.widget.FontAwesomeButton;
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
    @InjectView(R.id.tv_deliveryman)
    TextView tv_deliveryman;
    @InjectView(R.id.lv_order_goods_data)
    ListView lv_order_goods_data;
    @InjectView(R.id.fatv_destroy)
    FontAwesomeButton fabtn_destroy;
    @InjectView(R.id.fabtn_edit)
    FontAwesomeButton fatv_edit;
    @InjectView(R.id.fabtn_back)
    FontAwesomeButton fabtn_back;

    private IOrder order;

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
    }

    private void build_actionbar() {
        fatv_edit.setOnClickListener(this);
        fabtn_destroy.setOnClickListener(this);
        fabtn_back.setOnClickListener(this);
    }

    private void build_deliveryman() {
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
                edit_order();
                break;
            case R.id.fatv_destroy:
                destroy_order();
                break;
            case R.id.fabtn_back:
                finish();
                break;
        }
    }

    private void edit_order() {
        // todo goto edit order
        Intent intent = new Intent(OrderActivity.this, OrderEditActivity.class);
        intent.putExtra(Constants.Extra.ORDER, order);
        startActivityForResult(intent, Constants.Request.ORDER);
        System.out.println("edit_order");
    }

    private void destroy_order() {
        //todo destroy order
        System.out.println("destroy_order");
//        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constants.Request.ORDER:
                if(resultCode == RESULT_OK)
                {
                    System.out.println("changed");
                    // todo get new order from intent
                    //                    refresh()
                }
                break;
        }
    }
}