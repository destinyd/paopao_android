package com.realityandapp.paopao_customer.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IGood;
import com.realityandapp.paopao_customer.models.interfaces.IShopCart;
import com.realityandapp.paopao_customer.models.test.Shop;
import com.realityandapp.paopao_customer.models.test.ShopCart;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.views.adapter.GoodsAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class ShopGoodsActivity extends PaopaoBaseActivity {
    @InjectExtra(Constants.Extra.SHOP)
    private Shop shop;
    //    @InjectView(R.id.fabtn_back)
//    FontAwesomeButton fabtn_back;
//    @InjectView(R.id.fabtn_cart)
//    FontAwesomeButton fabtn_cart;
    @InjectView(R.id.lv_goods)
    ListView lv_goods;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    @InjectView(R.id.btn_submit)
    Button btn_submit;
    @InjectView(R.id.tv_goods_total)
    TextView tv_goods_total;
    private List<IGood> goods;
    private IShopCart shop_cart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods);


        setTitle(shop.get_name() + " 的商品");
        init_views();
        get_data();
    }

    private void init_views() {
        btn_submit.setOnClickListener(this);
    }

    private void get_data() {
        new RoboAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                goods = DataProvider.get_goods(shop.get_id());
                shop_cart = new ShopCart();
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
        build_list();
        build_total();
    }

    public void build_total() {
        tv_goods_total.setText(String.format(Constants.Format.PRICE, shop_cart.get_goods_total()));
        int amount = shop_cart.get_goods_amount();
        if (amount > 0) {
            btn_submit.setText(String.format(Constants.Format.BTN_TOTAL, shop_cart.get_goods_amount()));
            btn_submit.setEnabled(true);
        } else {
            btn_submit.setText("请点单");
            btn_submit.setEnabled(false);
        }
    }

    private void build_list() {
        final GoodsAdapter adapter =
                new GoodsAdapter(this, goods, shop_cart);
        lv_goods.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.fabtn_cart:
//                Intent intent = new Intent(this, CartActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.fabtn_back:
//                finish();
//                break;
            case R.id.btn_submit:
                submit();
                break;
            default:
                super.onClick(v);
        }
    }

    private void submit() {
        Intent intent = new Intent(this, ShopCartActivity.class);
        intent.putExtra(Constants.Extra.SHOP_CART, shop_cart);
        startActivityForResult(intent, Constants.Request.SHOP_CART);
    }
}