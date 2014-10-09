package com.realityandapp.paopao_customer.views;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(
                new SimpleAdapter(
                        this, getData(), android.R.layout.simple_list_item_1, new String[]{"title"},
                        new int[]{android.R.id.text1}
                )
        );
        getListView().setScrollbarFadingEnabled(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
        Intent intent = new Intent(this, (Class<? extends Activity>) map.get("activity"));
        startActivity(intent);
    }

    private List<? extends Map<String, ?>> getData() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        addItem(data, "pay", PayActivity.class);
        addItem(data, "addresses", AddressesActivity.class);
        addItem(data, "edit order", OrderEditActivity.class);
        addItem(data, "sign in", SignInActivity.class);
        addItem(data, "sign up", SignUpActivity.class);
        addItem(data, "shops", ShopsActivity.class);
        addItem(data, "goods grid", ShopGoodsGridActivity.class);
        addItem(data, "cart", CartActivity.class);
        addItem(data, "cart to order", CartToOrderActivity.class);
        addItem(data, "order", OrderActivity.class);
        addItem(data, "im", IMActivity.class);
        addItem(data, "new address", NewAddressActivity.class);

        return data;
    }

    private void addItem(List<Map<String, Object>> data, String title,
                         Class<? extends Activity> activityClass) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", data.size() + ". " + title);
        map.put("activity", activityClass);
        data.add(map);
    }
}