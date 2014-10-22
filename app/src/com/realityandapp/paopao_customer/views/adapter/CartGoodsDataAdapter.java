package com.realityandapp.paopao_customer.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class CartGoodsDataAdapter extends SingleTypeAdapter<ICartGoodsData> implements View.OnClickListener {

    private static final String FORMAT_UNIT_PRICE = "￥%.2f/%s";
    private final List<ICartGoodsData> cart_data;
    private List<Integer> amounts = new ArrayList<Integer>();

    public CartGoodsDataAdapter(LayoutInflater inflater,
                                final List<ICartGoodsData> items) {
        super(inflater, R.layout.cart_goods_data_list_item);
        cart_data = items;
        setItems(cart_data);
        init_amounts();
    }
    private void init_amounts() {
        for (int i = 0; i < cart_data.size(); i++) {
            amounts.add(cart_data.get(i).get_amount());
        }
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{
                R.id.cb_good, R.id.tv_amount, R.id.tv_unit_price, R.id.ll_amount, R.id.ll_amount_none,
                R.id.fabtn_minus, R.id.fabtn_add, R.id.tv_add_to_cart
        };
    }

    @Override
    protected void update(int position, ICartGoodsData item) {
        setText(0, item.get_good_name());
//        setText(1, String.valueOf(item.get_amount()));
        setText(2, String.format(FORMAT_UNIT_PRICE, item.get_price(), item.get_good_unit()));
        show_amount(amounts.get(position) > 0);
        setNumber(1, amounts.get(position));
    }


    private void show_amount(boolean is_show) {
        getView(3, LinearLayout.class).setVisibility(is_show ? View.VISIBLE : View.INVISIBLE);
        getView(4, LinearLayout.class).setVisibility(is_show ? View.INVISIBLE : View.VISIBLE);
    }

    private void init_font_awesome_button(int id, int position) {
        FontAwesomeButton fabtn = getView(id, FontAwesomeButton.class);
        fabtn.setTag(position);
        fabtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_minus:
                update_amount(v, -1);
                break;
            case R.id.fabtn_add:
                update_amount(v, 1);
                break;
            case R.id.tv_add_to_cart:
                update_amount(v, 1);
                break;
        }
    }

    private void update_amount(View v, Integer plus) {
        int position;
        Integer amount;
        position = (Integer) v.getTag();
        amount = amounts.get(position);
        amount += plus;
        set_amount(position, amount);
        update_amount_from_view(v, position);
        show_amount_from_view(v, amount > 0);
    }

    public void set_amount(int position, Integer amount) {
        if (amount >= 0 && amount <= 99)
            amounts.set(position, amount);
    }

    private void update_amount_from_view(View v, int position) {
        ((TextView) ((RelativeLayout) v.getParent().getParent()).findViewById(R.id.tv_amount)).setText(String.valueOf(amounts.get(position)));
    }

    private void show_amount_from_view(View v, boolean is_show) {
        RelativeLayout relativeLayout = (RelativeLayout) v.getParent().getParent();
        relativeLayout.findViewById(R.id.ll_amount).setVisibility(is_show ? View.VISIBLE : View.INVISIBLE);
        relativeLayout.findViewById(R.id.ll_amount_none).setVisibility(is_show ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        init_font_awesome_button(5, position);
        init_font_awesome_button(6, position);
        TextView textView = getView(7, TextView.class);
        textView.setTag(position);
        textView.setOnClickListener(this);
        return view;
    }
}
