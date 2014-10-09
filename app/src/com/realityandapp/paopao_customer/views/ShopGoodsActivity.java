package com.realityandapp.paopao_customer.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IGood;
import com.realityandapp.paopao_customer.models.test.Shop;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.views.adapter.GoodsAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class ShopGoodsActivity extends PaopaoBaseActivity implements View.OnClickListener {
    @InjectExtra(Constants.Extra.SHOP)
    private Shop shop;
    @InjectView(R.id.fabtn_back)
    FontAwesomeButton fabtn_back;
    @InjectView(R.id.fabtn_cart)
    FontAwesomeButton fabtn_cart;
    @InjectView(R.id.lv_goods)
    ListView lv_goods;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    private List<IGood> goods;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods);

        shop = new Shop();
        setTitle(shop.get_name() + " 的菜品");
        init_views();
        get_datas();
    }

    private void init_views() {
        fabtn_cart.setOnClickListener(this);
        fabtn_back.setOnClickListener(this);
    }

    private void get_datas() {
        new RoboAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                goods = DataProvider.get_goods(shop.get_id());
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                build_view();
                loading_view.hide();
                set_cart_count(99);
            }
        }.execute();
    }

    private void build_view() {
        final GoodsAdapter adapter =
                new GoodsAdapter(getLayoutInflater(), goods);
        lv_goods.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabtn_cart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.fabtn_back:
                finish();
                break;
        }
    }
}