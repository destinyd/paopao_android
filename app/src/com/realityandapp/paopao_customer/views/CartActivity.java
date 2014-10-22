package com.realityandapp.paopao_customer.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.ICart;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.ListViewUtils;
import com.realityandapp.paopao_customer.utils.PaopaoAsyncTask;
import com.realityandapp.paopao_customer.views.adapter.CartDataAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

/**
 * Created by dd on 14-9-18.
 */
public class CartActivity extends PaopaoBaseActivity implements View.OnClickListener {
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
    @InjectView(R.id.fabtn_back)
    FontAwesomeButton fabtn_back;
    private ICart cart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        setTitle("购物车");
        init_views();
        get_data();
    }

    private void init_views() {
        fabtn_back.setOnClickListener(this);
    }

    private void get_data() {
        new PaopaoAsyncTask<Void>(this) {

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
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
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
        tv_cart_total.setText(String.format(Constants.Format.PRICE, cart.get_total()));
        btn_submit.setText(String.format(Constants.Format.BTN_TOTAL, cart.get_amount_count()));
        btn_submit.setOnClickListener(this);
    }

    private void build_lv_cart_data() {
        CartDataAdapter adapter =
                new CartDataAdapter(getLayoutInflater(), cart.get_data());
        lv_cart_data.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(lv_cart_data);
    }

    private void build_cart_goods_count() {
        cb_cart_goods_count.setText(String.format(Constants.Format.CART_GOODS_COUNT, cart.get_goods_type_count()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                go_to_cart_to_order();
                break;
            case R.id.fabtn_back:
                finish();
                break;
        }
    }

    private void go_to_cart_to_order() {
        Intent intent = new Intent(this, CartToOrderActivity.class);
        intent.putExtra(Constants.Extra.CART, cart);
        startActivityForResult(intent, Constants.Request.CART);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.Request.CART:
                if(resultCode == RESULT_OK)
                    get_data(); // refresh if cart already to order
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}