package com.realityandapp.paopao_customer.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IAddress;
import com.realityandapp.paopao_customer.models.interfaces.ICart;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.ListViewUtils;
import com.realityandapp.paopao_customer.views.adapter.CartToOrderAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class CartToOrderActivity extends PaopaoBaseActivity implements View.OnClickListener {
    @InjectExtra(Constants.Extra.CART)
    ICart cart;

    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    @InjectView(R.id.btn_submit)
    Button btn_submit;
    @InjectView(R.id.tv_cart_to_order_total)
    TextView tv_cart_to_order_total;
    @InjectView(R.id.tv_contact)
    TextView tv_contact;
    @InjectView(R.id.tv_address)
    TextView tv_address;
    @InjectView(R.id.tv_edit_address)
    TextView tv_edit_address;
    @InjectView(R.id.tv_add_address)
    TextView tv_add_address;
    @InjectView(R.id.lv_cart_to_order_data)
    ListView lv_cart_to_order_data;
    @InjectView(R.id.fabtn_back)
    FontAwesomeButton fabtn_back;

    private IAddress address = null;
    private List<String> list_address_string = new ArrayList<String>();
    private ArrayAdapter<String> addressesAdapter;
    AlertDialog addressesDialog;
    private List<IAddress> addresses = new ArrayList<IAddress>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_to_order);

        setTitle("订单确定");
        get_data();
    }

    private void get_data() {
        new RoboAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                address = DataProvider.get_default_address();
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                build_views();
                loading_view.hide();
            }
        }.execute();
    }

    private void build_views() {
        build_total();
        build_once();
        build_address();
        build_cart_to_order();
    }

    private void build_once() {
        tv_edit_address.setOnClickListener(this);
        tv_add_address.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        fabtn_back.setOnClickListener(this);
    }

    private void build_cart_to_order() {
        CartToOrderAdapter adapter =
                new CartToOrderAdapter(getLayoutInflater(), cart.get_data());
        lv_cart_to_order_data.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(lv_cart_to_order_data);
    }

    private void build_total() {
        tv_cart_to_order_total.setText(String.format(Constants.Format.PRICE, cart.get_total()));
    }

    private void build_address() {
        if(address == null){
            tv_address.setVisibility(View.GONE);
            tv_contact.setText("暂无地址信息");
            tv_edit_address.setVisibility(View.GONE);
            tv_add_address.setVisibility(View.VISIBLE);
            btn_submit.setEnabled(false);
        }
        else {
            tv_contact.setText(String.format(Constants.Format.CONTACT, address.get_realname(), address.get_phone()));
            tv_address.setVisibility(View.VISIBLE);
            tv_address.setText(address.get_address());
            tv_edit_address.setVisibility(View.VISIBLE);
            tv_add_address.setVisibility(View.GONE);
            btn_submit.setEnabled(true);
        }
    }

    Integer selection = -1;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_address:
                get_addresses();
                break;
            case R.id.tv_add_address:
                go_to_new_address();
                break;
            case R.id.btn_submit:
                submit();
                break;
            case R.id.fabtn_back:
                finish();
                break;
        }
    }



    private void get_addresses() {
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
                show_and_select_address();
                loading_view.hide();
            }
        }.execute();
    }

    private void show_and_select_address() {
        for (IAddress address : addresses) {
            list_address_string.add(String.format(
                    Constants.Format.FULL_CONTACT, address.get_address(), address.get_realname(), address.get_phone()
            ));
        }
        addressesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list_address_string);

        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(CartToOrderActivity.this)
                .setTitle("请选择您所在地址")
                .setAdapter(addressesAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                                select_address = list_address_string.get(which);
                        selection = which;
                        refresh_for_change_address_to(addresses.get(which));
                        dialog.cancel();
                    }
                })
                .setNegativeButton("新建地址", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        go_to_new_address();
                    }
                })
                .setNeutralButton("取消", null);
        addressesDialog = dialog_builder.create();
        addressesDialog.show();
        addressesDialog.getListView().setSelection(0);
    }

    private void refresh_for_change_address_to(IAddress iAddress) {
        address = iAddress;
        build_address();
    }

    private void submit() {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }

    private void go_to_new_address() {
        Intent intent = new Intent(this, NewAddressActivity.class);
        startActivityForResult(intent, Constants.Request.ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.Request.ADDRESS:
                if (resultCode == RESULT_OK) {
                    IAddress address = (IAddress) data.getSerializableExtra(Constants.Extra.ADDRESS);
                    refresh_for_change_address_to(address);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}