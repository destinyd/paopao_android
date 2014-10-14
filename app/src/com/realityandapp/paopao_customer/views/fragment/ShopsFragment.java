package com.realityandapp.paopao_customer.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.easemob.chat.EMChatManager;
import com.mindpin.android.loadingview.LoadingView;
import com.readystatesoftware.viewbadger.BadgeView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IShop;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.views.ShopGoodsActivity;
import com.realityandapp.paopao_customer.views.adapter.ShopsAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseFragment;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

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

    private void get_datas() {
        new RoboAsyncTask<Void>(getActivity()){

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
                intent.putExtra(Constants.Extra.SHOP, shops.get(i));
                startActivity(intent);
            }
        });
    }
}