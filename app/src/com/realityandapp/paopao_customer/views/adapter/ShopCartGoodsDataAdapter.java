package com.realityandapp.paopao_customer.views.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.http.CartGoodsData;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class ShopCartGoodsDataAdapter extends SingleTypeAdapter<ICartGoodsData> implements View.OnClickListener {
    private final List<CartGoodsData> cart_data;
    private final LayoutInflater inflater;
    private String[] list_plus_tags;
    private ArrayAdapter<String> addressesAdapter;
    Integer selection = -1;
    private AlertDialog addressesDialog;

    public ShopCartGoodsDataAdapter(LayoutInflater inflater,
                                    final List<CartGoodsData> items) {
        super(inflater, R.layout.cart_to_order_goods_data_list_item);
        this.inflater = inflater;
        cart_data = items;
        init();
        setItems(cart_data);
    }

    private void init() {
        list_plus_tags = inflater.getContext().getResources().getStringArray(R.array.plus_tags);
        addressesAdapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_spinner_dropdown_item, list_plus_tags);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.tv_good_name, R.id.tv_unit_price, R.id.tv_good_total, R.id.fabtn_add_plus_tag };
    }

    @Override
    protected void update(int position, ICartGoodsData item) {
        setText(0, item.get_name());
        setText(1, String.format(Constants.Format.TOTAL_CALCULATE, item.get_price(), item.get_amount(), item.get_unit()));
        setText(2, String.format(Constants.Format.PRICE, item.get_amount() * item.get_price()));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        FontAwesomeButton fa_btn = getView(3, FontAwesomeButton.class);
        fa_btn.setTag(position);
        fa_btn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.fabtn_add_plus_tag:

                AlertDialog.Builder dialog_builder = new AlertDialog.Builder(inflater.getContext())
                        .setTitle("请选择您所在地址")
                        .setAdapter(addressesAdapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                select_address = list_plus_tags.get(which);
                                selection = which;
                                String plus_tag = list_plus_tags[which];
                                System.out.println(plus_tag);
                                EditText editText =
                                        (EditText) ((ViewGroup) v.getParent()).findViewById(R.id.et_plus);
                                String str_plus = editText.getText().toString();
                                if(str_plus.length() >0 )
                                    str_plus += ",";
                                str_plus += plus_tag;
                                editText.setText(str_plus);
                                dialog.cancel();
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
