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
    @InjectView(R.id.fabtn_back)
    FontAwesomeButton fatv_back;
    @InjectView(R.id.fatv_add)
    FontAwesomeButton fatv_add;
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
        fatv_back.setOnClickListener(this);
        fatv_add.setOnClickListener(this);
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
                //todo change default address
//                Intent intent = new Intent(ShopGoodsActivity.this, Good.class);
//                intent.putExtra(Constants.Extra.GOOD, goods.get(i));
//                startActivity(intent);
                Toast.makeText(AddressesActivity.this
                        ,"默认地址修改为：\n" + String.format(Constants.Format.FORMAT_FULL_CONTACT_TOAST
                                , address.get_address(), address.get_realname(), address.get_phone())
                        , Toast.LENGTH_LONG
                ).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabtn_back:
                finish();
                break;
            case R.id.fatv_add:
                startActivityForResult(new Intent(this, NewAddressActivity.class), Constants.Request.ADDRESS);
                break;
        }
    }
}