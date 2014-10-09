package com.realityandapp.paopao_customer.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IGood;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class GoodsAdapter extends //MultiTypeAdapter implements View.OnClickListener{
        SingleTypeAdapter<IGood> implements View.OnClickListener {
//        SectionSingleTypeAdapter<IGood>{//} implements View.OnClickListener {

    private final List<IGood> goods;
    private final List<Integer> goods_amount = new ArrayList<Integer>();
    private final LayoutInflater inflater;

    public GoodsAdapter(LayoutInflater inflater,
                        final List<IGood> items) {
        super(inflater, R.layout.goods_list_item);
        this.inflater = inflater;
        goods = items;
        setItems(goods);
        init_goods_amount();
    }

    private void init_goods_amount() {
        for (int i = 0; i < goods.size(); i++) {
            goods_amount.add(0);
        }
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{
                R.id.iv_good_icon, R.id.good_title, R.id.good_price, R.id.good_desc, R.id.tv_amount,
                R.id.fabtn_minus, R.id.fabtn_add, R.id.tv_add_to_cart, R.id.ll_amount, R.id.ll_amount_none};
    }

    @Override
    protected void update(int position, IGood item) {
        ImageLoader.getInstance().displayImage(item.get_image(), imageView(0));
        setText(1, item.get_name());
        setText(2, String.format(Constants.Format.FORMAT_PRICE_WITH_UNIT, item.get_price(), item.get_unit()));
        setText(3, item.get_description());
        show_amount(goods_amount.get(position) > 0);
        setNumber(4, goods_amount.get(position));
    }

    private void show_amount(boolean is_show) {
        getView(8, LinearLayout.class).setVisibility(is_show ? View.VISIBLE : View.INVISIBLE);
        getView(9, LinearLayout.class).setVisibility(is_show ? View.INVISIBLE : View.VISIBLE);
    }

    private void init_font_awesome_button(int id, int position) {
        FontAwesomeButton fabtn = getView(id, FontAwesomeButton.class);
        fabtn.setTag(position);
        fabtn.setOnClickListener(this);
    }

    //
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
        amount = goods_amount.get(position);
        amount += plus;
        set_amount(position, amount);
        update_amount_from_view(v, position);
        show_amount_from_view(v, amount > 0);
    }

    private void show_amount_from_view(View v, boolean is_show) {
        RelativeLayout relativeLayout = (RelativeLayout) v.getParent().getParent();
        relativeLayout.findViewById(R.id.ll_amount).setVisibility(is_show ? View.VISIBLE : View.INVISIBLE);
        relativeLayout.findViewById(R.id.ll_amount_none).setVisibility(is_show ? View.INVISIBLE : View.VISIBLE);
    }

    private void update_amount_from_view(View v, int position) {
        ((TextView) ((RelativeLayout) v.getParent().getParent()).findViewById(R.id.tv_amount)).setText(String.valueOf(goods_amount.get(position)));
    }

    public Integer get_amount(int position) {
        return goods_amount.get(position);
    }

    public void set_amount(int position, Integer amount) {
        if (amount >= 0 && amount <= 99)
            goods_amount.set(position, amount);
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
