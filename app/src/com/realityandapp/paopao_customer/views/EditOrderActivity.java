package com.realityandapp.paopao_customer.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IAddress;
import com.realityandapp.paopao_customer.models.interfaces.IOrder;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.ListViewUtils;
import com.realityandapp.paopao_customer.utils.PaopaoAsyncTask;
import com.realityandapp.paopao_customer.views.adapter.EditOrderGoodsDataAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class EditOrderActivity extends PaopaoBaseActivity {
    @InjectExtra(Constants.Extra.ORDER_ID)
    String order_id;
    IOrder order;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    @InjectView(R.id.tv_order_total)
    TextView tv_order_total;
    @InjectView(R.id.tv_contact)
    TextView tv_contact;
    @InjectView(R.id.tv_address)
    TextView tv_address;
    @InjectView(R.id.tv_edit_address)
    TextView tv_edit_address;
    @InjectView(R.id.tv_add_address)
    TextView tv_add_address;
    @InjectView(R.id.tv_delivery_price)
    TextView tv_delivery_price;
    @InjectView(R.id.lv_order_data)
    ListView lv_order_data;

    private List<String> list_address_string = new ArrayList<String>();
    private ArrayAdapter<String> addressesAdapter;
    AlertDialog addressesDialog;
    private List<IAddress> addresses = null;
    //    private IOrder order;
    private IAddress selected_address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_order);

        setTitle("订单修改");
        get_data();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bind_actions();
    }

    private void bind_actions() {
        findViewById(R.id.fabtn_cancel).setOnClickListener(this);
        findViewById(R.id.fatv_submit).setOnClickListener(this);
    }

    private void get_data() {
        new PaopaoAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                order = DataProvider.my_order(order_id);
                selected_address = order.get_address();
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                build_views();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                loading_view.hide();
            }
        }.execute();
    }


    private void get_addresses() {
        new PaopaoAsyncTask<Void>(this) {

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
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
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

        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(EditOrderActivity.this)
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
        selected_address = iAddress;
        build_address();
    }

    private void build_views() {
        build_total();
        build_once();
        build_address();
        build_order();
    }

    private void build_once() {
        tv_edit_address.setOnClickListener(this);
        tv_add_address.setOnClickListener(this);
    }

    private void build_order() {
        EditOrderGoodsDataAdapter adapter =
                new EditOrderGoodsDataAdapter(getLayoutInflater(), order.get_order_items());
        lv_order_data.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(lv_order_data);
    }

    private void build_total() {
        tv_order_total.setText(String.format(Constants.Format.PRICE, order.get_total()));
    }

    private void build_address() {
        if(selected_address != null) {
            tv_contact.setText(String.format(Constants.Format.CONTACT, selected_address.get_realname(), selected_address.get_phone()));
            tv_address.setText(selected_address.get_address());
        }
        tv_address.setVisibility(View.VISIBLE);
        tv_edit_address.setVisibility(View.VISIBLE);
        build_delivery_price();
    }

    private void build_delivery_price() {
        tv_delivery_price.setText(String.format(Constants.Format.PRICE, order.get_delivery_price()));
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
            case R.id.fatv_submit:
                submit();
                break;
            case R.id.fabtn_cancel:
                if (!selected_address.get_id().equals(order.get_address().get_id())) {
                    new AlertDialog.Builder(EditOrderActivity.this)
                            .setTitle("提示：订单地址已修改")
                            .setNegativeButton("取消修改", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setNeutralButton("继续修改", null)
                            .create()
                            .show();
                } else
                    finish();
                break;
        }
    }

    private void submit() {
        new PaopaoAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                order.set_address(selected_address);
                order.set_to_id(selected_address.get_id());
                order.save();
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                loading_view.hide();
            }
        }.execute();
    }

    private void go_to_new_address() {
        Intent intent = new Intent(this, NewAddressActivity.class);
        startActivityForResult(intent, Constants.Request.ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.Request.ADDRESS:
                if (resultCode == RESULT_OK && null != data) {
                    IAddress address = (IAddress) data.getSerializableExtra(Constants.Extra.ADDRESS);
                    refresh_for_change_address_to(address);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}