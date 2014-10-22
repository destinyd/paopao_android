package com.realityandapp.paopao_customer.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.http.ShopCart;
import com.realityandapp.paopao_customer.models.interfaces.IAddress;
import com.realityandapp.paopao_customer.models.interfaces.IOrder;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.ListViewUtils;
import com.realityandapp.paopao_customer.utils.PaopaoAsyncTask;
import com.realityandapp.paopao_customer.views.adapter.ShopCartGoodsDataAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class ShopCartActivity extends PaopaoBaseActivity {
    @InjectExtra(Constants.Extra.SHOP_CART)
    ShopCart shop_cart;

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
    @InjectView(R.id.lv_cart_goods)
    ListView lv_cart_goods;
    @InjectView(R.id.tv_delivery_price)
    TextView tv_delivery_price;
    @InjectView(R.id.tv_distance)
    TextView tv_distance;
    @InjectView(R.id.tv_shop_name)
    TextView tv_shop_name;

    private IAddress address = null;
    private List<String> list_address_string = new ArrayList<String>();
    private ArrayAdapter<String> addressesAdapter;
    AlertDialog addressesDialog;
    private List<IAddress> addresses = new ArrayList<IAddress>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_cart);

        setTitle("订单确定");
        get_data();
    }

    private void get_data() {
        new PaopaoAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                address = DataProvider.get_default_address();
                System.out.println("address:" + address);
                if (address != null) {
                    shop_cart.set_to_id(address.get_id());
                    shop_cart.calculate_distance_and_pricing(shop_cart.get_shop_id(), address.get_id());
                }
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

    private void build_views() {
        build_total();
        build_once();
        build_address();
        build_cart_to_order();
        build_delivery();
    }

    private void build_delivery() {
        tv_delivery_price.setText(String.format(Constants.Format.PRICE, shop_cart.get_delivery_price()));
        if(shop_cart.get_shop_name() != null)
            tv_shop_name.setText("距离" + shop_cart.get_shop_name());
        if (shop_cart.get_distance() != null) {
            tv_distance.setText(String.format(Constants.Format.DISTANCT, shop_cart.get_distance()));
        } else {
            tv_distance.setText("请先选择地址");
        }
    }

    private void build_once() {
        tv_edit_address.setOnClickListener(this);
        tv_add_address.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    private void build_cart_to_order() {
        ShopCartGoodsDataAdapter adapter =
                new ShopCartGoodsDataAdapter(getLayoutInflater(), shop_cart.get_cart_items());
        lv_cart_goods.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(lv_cart_goods);
    }

    private void build_total() {
        tv_cart_to_order_total.setText(String.format(Constants.Format.PRICE, shop_cart.get_total()));
    }

    private void build_address() {
        if (address == null) {
            tv_address.setVisibility(View.GONE);
            tv_contact.setText("暂无地址信息");
            tv_edit_address.setVisibility(View.GONE);
            tv_add_address.setVisibility(View.VISIBLE);
            btn_submit.setEnabled(false);
        } else {
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
                submit_cart();
                break;
            default:
                super.onClick(view);
        }
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

        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(ShopCartActivity.this)
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
        shop_cart.set_to_id(address.get_id());
        new PaopaoAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                shop_cart.calculate_distance_and_pricing(shop_cart.get_shop_id(), address.get_id());
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                build_address();
                build_delivery();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                loading_view.hide();
            }
        }.execute();
    }

    private void submit_cart() {
        new PaopaoAsyncTask<IOrder>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public IOrder call() throws Exception {
                return DataProvider.shop_cart_to_order(shop_cart);
            }

            @Override
            protected void onSuccess(IOrder order) throws Exception {
                if (order != null) {
                    return_order_id(order);
                } else {
                    Toast.makeText(getContext(), "提交失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                loading_view.hide();
            }
        }.execute();
    }

    private void return_order_id(IOrder order) {
        Intent intent = new Intent();
        intent.putExtra(Constants.Extra.ORDER_ID, order.get_id());
        setResult(RESULT_OK, intent);
//
//        Intent intent = new Intent(this, OrderActivity.class);
//        intent.putExtra(Constants.Extra.ORDER_ID, order.get_id());
//        startActivity(intent);
        finish();
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