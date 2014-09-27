package com.realityandapp.paopao_customer.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.ICart;
import com.realityandapp.paopao_customer.models.test.Cart;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.ListViewUtils;
import com.realityandapp.paopao_customer.views.adapter.CartDataAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

/**
 * Created by dd on 14-9-18.
 */
public class CartActivity extends PaopaoBaseActivity {
    private static final String FORMAT_PRICE = "￥%.2f";
    private static final String FORMAT_BTN_TOTAL = "结算(%d)";
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    @InjectView(R.id.cb_cart_goods_count)
    CheckBox cb_cart_goods_count;
    @InjectView(R.id.lv_cart_data)
    ListView lv_cart_data;
    @InjectView(R.id.tv_cart_total)
    TextView tv_cart_total;
    @InjectView(R.id.btn_submit)
    Button btn_submit;
    private ICart cart;

    private static String FORMAT_CART_GOODS_COUNT = "共%d件商品";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        setTitle("购物车");
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
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                build_view();
                loading_view.hide();
            }
        }.execute();
    }

    private void build_view() {
        build_cart_goods_count();
        build_lv_cart_data();
        build_rl_total();
    }

    private void build_rl_total() {
        tv_cart_total.setText(String.format(FORMAT_PRICE, cart.get_total()));
        btn_submit.setText(String.format(FORMAT_BTN_TOTAL, cart.get_amount_count()));
    }

    private void build_lv_cart_data() {
        CartDataAdapter adapter =
                new CartDataAdapter(getLayoutInflater(), cart.get_data());
        lv_cart_data.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(lv_cart_data);
    }

    private void build_cart_goods_count() {
        cb_cart_goods_count.setText(String.format(FORMAT_CART_GOODS_COUNT, cart.get_goods_type_count()));
    }
}