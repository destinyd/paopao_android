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
import com.realityandapp.paopao_customer.models.interfaces.IAddress;
import com.realityandapp.paopao_customer.models.interfaces.IOrder;
import com.realityandapp.paopao_customer.models.test.Address;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.utils.ListViewUtils;
import com.realityandapp.paopao_customer.views.adapter.OrderGoodsDataAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class OrderEditActivity extends PaopaoBaseActivity implements View.OnClickListener {
    private static final String FORMAT_PRICE = "￥%.2f";
    private static final String FORMAT_CONTACT = "%s(%s)";
    private static final String FORMAT_FULL_CONTACT = "%s %s(%s)";
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
    private ArrayList<Address> addresses = new ArrayList<Address>();
    private IOrder order;
    private IAddress selected_address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_edit);

        bind_actions();
        setTitle("订单修改");
        get_data();
    }

    private void bind_actions() {
        findViewById(R.id.fatv_cancel).setOnClickListener(this);
        findViewById(R.id.fatv_submit).setOnClickListener(this);
    }

    private void get_data() {
        new RoboAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                order = DataProvider.get_order("1");
                selected_address = order.get_address();
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
        build_order();
    }

    private void build_once() {
        tv_edit_address.setOnClickListener(this);
        tv_add_address.setOnClickListener(this);
    }

    private void build_order() {
        OrderGoodsDataAdapter adapter =
                new OrderGoodsDataAdapter(getLayoutInflater(), order.get_goods_data());
        lv_order_data.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(lv_order_data);
    }

    private void build_total() {
        tv_order_total.setText(String.format(FORMAT_PRICE, order.get_total()));
    }

    private void build_address() {
        tv_contact.setText(String.format(FORMAT_CONTACT, selected_address.get_realname(), selected_address.get_phone()));
        tv_address.setVisibility(View.VISIBLE);
        tv_address.setText(selected_address.get_address());
        tv_edit_address.setVisibility(View.VISIBLE);
        build_delivery_price();
    }

    private void build_delivery_price() {
        tv_delivery_price.setText(String.format(FORMAT_PRICE, order.get_delivery_price()));
    }

    Integer selection = -1;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_address:
                //todo a progress dialog to loading addresses
                if (addresses.size() == 0)
                    for (int i = 0; i < 5; i++) {
                        addresses.add(new Address());
                    }
                for (Address address : addresses) {
                    list_address_string.add(String.format(
                            FORMAT_FULL_CONTACT, order.get_address().get_address(), order.get_address().get_realname(), order.get_address().get_phone()
                    ));
                }
                addressesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list_address_string);

                AlertDialog.Builder dialog_builder = new AlertDialog.Builder(OrderEditActivity.this)
                        .setTitle("请选择您所在地址")
                        .setAdapter(addressesAdapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                select_address = list_address_string.get(which);
                                selection = which;
                                selected_address = addresses.get(which);
                                System.out.println(selected_address);
                                build_address();
                                //todo calculate delivery price for new address
//                                System.out.println(select_address);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("新建地址", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //todo goto new address activity
                                goto_new_address();
                            }
                        })
                        .setNeutralButton("取消", null);
                addressesDialog = dialog_builder.create();
                addressesDialog.show();
                addressesDialog.getListView().setSelection(0);
                break;
            case R.id.tv_add_address:
                goto_new_address();
                break;
            case R.id.fatv_submit:
                submit();
                break;
            case R.id.fatv_cancel:
                if(!selected_address.get_id().equals(order.get_address().get_id())){
                    new AlertDialog.Builder(OrderEditActivity.this)
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
                }
                else
                    finish();
                break;
        }
    }

    private void submit() {
        System.out.println("submit");
        //todo submit edit new address for order
    }

    private void goto_new_address() {
        System.out.println("新建地址");
    }
}