package com.realityandapp.paopao_customer.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.test.Address;
import com.realityandapp.paopao_customer.models.test.Cart;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.ListViewUtils;
import com.realityandapp.paopao_customer.views.adapter.CartDataAdapter;
import com.realityandapp.paopao_customer.views.adapter.CartToOrderAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

/**
 * Created by dd on 14-9-18.
 */
public class CarToOrderActivity extends PaopaoBaseActivity {
    private static final String FORMAT_PRICE = "￥%.2f";
    private static final String FORMAT_CONTACT = "%s(%s)";
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    @InjectView(R.id.btn_submit)
    Button btn_submit;
    @InjectView(R.id.tv_cart_to_order_total)
    TextView tv_cart_to_order_total;
    @InjectView(R.id.tv_contact)
    TextView tv_contact;
    @InjectView(R.id.tv_address)
    TextView tv_address;
    @InjectView(R.id.lv_cart_to_order_data)
    ListView lv_cart_to_order_data;

    private Cart cart;
    private Address address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_to_order);

        setTitle("订单确定");
        get_data();
    }

    private void get_data() {
        new RoboAsyncTask<Void>(this){

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                cart = DataProvider.get_cart();
                address = DataProvider.get_default_address();
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
        build_total();
        build_address();
        build_cart_to_order();
    }

    private void build_cart_to_order() {
        CartToOrderAdapter adapter =
                new CartToOrderAdapter(getLayoutInflater(), cart.get_data());
        lv_cart_to_order_data.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(lv_cart_to_order_data);
    }

    private void build_total() {
        tv_cart_to_order_total.setText(String.format(FORMAT_PRICE, cart.get_total()));
    }

    private void build_address() {
        tv_contact.setText(String.format(FORMAT_CONTACT, address.get_realname(), address.get_phone()));
        tv_address.setText(address.get_address());
    }
}