package com.realityandapp.paopao_customer.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IShop;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.PaopaoAsyncTask;
import com.realityandapp.paopao_customer.views.ShopGoodsActivity;
import com.realityandapp.paopao_customer.views.adapter.ShopsAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseFragment;
import roboguice.inject.InjectView;

import java.util.List;

/**
 * Created by dd on 14-10-14.
 */
public class ShopsFragment extends PaopaoBaseFragment {
    @InjectView(R.id.gv_shops)
    GridView gv_shops;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    private List<IShop> shops;
    //    @InjectView(R.id.fabtn_cart)
//    FontAwesomeButton fabtn_cart;
    private boolean loaded = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shops, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("您周围的餐厅");
        get_datas();
    }

    public  void get_datas() {
        System.out.println("loaded:" + loaded);
        if(!loaded)
        new PaopaoAsyncTask<Void>(getActivity()){

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
                loaded = true;
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                loading_view.hide();
            }
        }.execute();
    }

    private void build_view() {
        ShopsAdapter adapter =
                new ShopsAdapter(getActivity().getLayoutInflater(), shops);
        gv_shops.setAdapter(adapter);
        gv_shops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShopGoodsActivity.class);
                intent.putExtra(Constants.Extra.SHOP_ID, shops.get(i).get_id());
                intent.putExtra(Constants.Extra.SHOP_NAME, shops.get(i).get_name());
                startActivity(intent);
            }
        });
    }
}