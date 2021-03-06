package com.realityandapp.paopao_customer.views;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IGood;
import com.realityandapp.paopao_customer.models.http.Shop;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.PaopaoAsyncTask;
import com.realityandapp.paopao_customer.views.adapter.GoodsGridAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class ShopGoodsGridActivity extends PaopaoBaseActivity {
    @InjectExtra(Constants.Extra.SHOP)
    private Shop shop;
    @InjectView(R.id.gv_shops)
    GridView gv_shops;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    private List<IGood> goods;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_grid);

        shop = new Shop();
        setTitle(shop.get_name() + " 的菜品");
        get_datas();
    }

    private void get_datas() {
        new PaopaoAsyncTask<Void>(this){

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
//                set_cart_count(99);
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                loading_view.hide();
            }
        }.execute();
    }

    private void build_view() {
        GoodsGridAdapter adapter =
                new GoodsGridAdapter(getLayoutInflater(), goods);
        gv_shops.setAdapter(adapter);
        gv_shops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(ShopGoodsActivity.this, Good.class);
//                intent.putExtra(Constants.Extra.GOOD, goods.get(i));
//                startActivity(intent);
            }
        });

    }
}