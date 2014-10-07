package com.realityandapp.paopao_customer.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IShop;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.views.adapter.ShopsAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseIncludeDrawerActivity;
import com.realityandapp.paopao_customer.views.widget.FontAwesomeButton;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class ShopsActivity extends PaopaoBaseIncludeDrawerActivity {
    @InjectView(R.id.gv_shops)
    GridView gv_shops;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    private List<IShop> shops;
    @InjectView(R.id.fabtn_cart)
    FontAwesomeButton fabtn_cart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shops);
        setTitle("您周围的餐厅");
        init_views();
        get_datas();
    }

    private void init_views() {
        fabtn_cart.setOnClickListener(this);
    }

    private void get_datas() {
        new RoboAsyncTask<Void>(this){

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                shops = DataProvider.get_shops();
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                build_view();
                loading_view.hide();
                set_cart_count(101);
            }
        }.execute();
    }

    private void build_view() {
        ShopsAdapter adapter =
                new ShopsAdapter(getLayoutInflater(), shops);
        gv_shops.setAdapter(adapter);
        gv_shops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ShopsActivity.this, ShopGoodsActivity.class);
                intent.putExtra(Constants.Extra.SHOP, shops.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabtn_cart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return;
        }
        super.onClick(v);
    }
}