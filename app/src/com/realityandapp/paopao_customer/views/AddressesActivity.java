package com.realityandapp.paopao_customer.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IAddress;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.PaopaoAsyncTask;
import com.realityandapp.paopao_customer.views.adapter.AddressesAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class AddressesActivity extends PaopaoBaseActivity implements View.OnClickListener {
    @InjectView(R.id.list)
    ListView list;
    @InjectView(R.id.fabtn_add)
    FontAwesomeButton fabtn_add;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    private List<IAddress> addresses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addresses);

        setTitle("我的地址簿");
        bind_views();
        get_datas();
    }

    private void bind_views() {
        fabtn_add.setOnClickListener(this);
    }

    private void get_datas() {
        new RoboAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                addresses = DataProvider.get_addresses();
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
        if(addresses.size() > 0) {
            final AddressesAdapter adapter =
                    new AddressesAdapter(getLayoutInflater(), addresses, addresses.get(0));
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    IAddress address = adapter.getItem(i);
                    adapter.set_address_default(address);
                    adapter.notifyDataSetChanged();
                    System.out.println("default address:" + adapter.get_address_default().get_address());
                    set_default_address(address);
                }
            });
        }
    }

    private void set_default_address(final IAddress address) {
        new PaopaoAsyncTask<Boolean>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                super.onPreExecute();
                loading_view.show();
            }

            @Override
            public Boolean call() throws Exception {
                IAddress iaddress = DataProvider.set_default_address(address.get_id());
                return iaddress != null;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                Toast.makeText(AddressesActivity.this
                        ,"设置默认地址出错,请检查网络是否畅通。"
                        , Toast.LENGTH_LONG
                ).show();
            }

            @Override
            protected void onSuccess(Boolean b) throws Exception {
                if(b)
                    Toast.makeText(AddressesActivity.this
                            ,"默认地址修改为：\n" + String.format(Constants.Format.FULL_CONTACT_TOAST
                                    , address.get_address(), address.get_realname(), address.get_phone())
                            , Toast.LENGTH_LONG
                    ).show();
                else
                    Toast.makeText(AddressesActivity.this
                            ,"设置默认地址失败"
                            , Toast.LENGTH_LONG
                    ).show();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                loading_view.hide();
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabtn_add:
                startActivityForResult(new Intent(this, NewAddressActivity.class), Constants.Request.ADDRESS);
                break;
            default:
                super.onClick(view);
        }
    }
}