package com.realityandapp.paopao_customer.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.test.Address;
import com.realityandapp.paopao_customer.models.test.Cart;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.ListViewUtils;
import com.realityandapp.paopao_customer.views.adapter.CartToOrderAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class CarToOrderActivity extends PaopaoBaseActivity implements View.OnClickListener {
    private static final String FORMAT_PRICE = "￥%.2f";
    private static final String FORMAT_CONTACT = "%s(%s)";
    private static final String FORMAT_FULL_CONTACT = "%s %s(%s)";
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
    @InjectView(R.id.lv_cart_to_order_data)
    ListView lv_cart_to_order_data;

    private Cart cart;
    private Address address;
    private List<String> list_address_string = new ArrayList<String>();
    private ArrayAdapter<String> addressesAdapter;
    AlertDialog addressesDialog;
    private ArrayList<Address> addresses = new ArrayList<Address>();

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
                cart = DataProvider.get_cart();
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
        build_address();
        build_cart_to_order();
    }

    private void build_cart_to_order() {
        CartToOrderAdapter adapter =
                new CartToOrderAdapter(getLayoutInflater(), cart.get_data());
        lv_cart_to_order_data.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(lv_cart_to_order_data);
    }

    private void build_total() {
        tv_cart_to_order_total.setText(String.format(FORMAT_PRICE, cart.get_total()));
    }

    private void build_address() {
        tv_contact.setText(String.format(FORMAT_CONTACT, address.get_realname(), address.get_phone()));
        tv_address.setText(address.get_address());
        tv_edit_address.setOnClickListener(this);
    }

    Integer selection = -1;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_address:
                //todo a progress dialog to loading addresses
                if (addresses.size() == 0)
                    for (int i = 0; i < 20; i++) {
                        addresses.add(new Address());
                    }
                for (Address address : addresses) {
                    list_address_string.add(String.format(
                            FORMAT_FULL_CONTACT, address.get_address(), address.get_realname(), address.get_phone()
                    ));
                }
                addressesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list_address_string);

                AlertDialog.Builder dialog_builder = new AlertDialog.Builder(CarToOrderActivity.this)
                        .setTitle("请选择您所在地址")
                        .setAdapter(addressesAdapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                select_address = list_address_string.get(which);
                                selection = which;
                                address = addresses.get(which);
                                build_address();
//                                System.out.println(select_address);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("新建地址", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //todo goto new address activity
                                System.out.println("新建地址");
                            }
                        })
                        .setNeutralButton("取消", null);
                addressesDialog = dialog_builder.create();
                addressesDialog.show();
                addressesDialog.getListView().setSelection(0);
                break;
        }
    }
}