package com.realityandapp.paopao_customer.views;

import android.os.Bundle;
import android.widget.GridView;
import com.mindpin.android.loadingview.LoadingView;
import com.readystatesoftware.viewbadger.BadgeView;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IShop;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.views.adapter.ShopsAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import com.realityandapp.paopao_customer.views.widget.FontAwesomeTextView;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class ShopsActivity extends PaopaoBaseActivity {
    @InjectView(R.id.gv_shops)
    GridView gv_shops;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    private List<IShop> shops;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shops);
        setTitle("您周围的餐厅");
        get_datas();
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

    }
}