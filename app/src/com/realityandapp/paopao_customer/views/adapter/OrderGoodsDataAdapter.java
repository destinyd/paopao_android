package com.realityandapp.paopao_customer.views.adapter;

import android.view.LayoutInflater;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class OrderGoodsDataAdapter extends SingleTypeAdapter<ICartGoodsData> {
    private static final String FORMAT_PRICE = "￥%.2f";
    private static final String FORMAT_TOTAL_CALCULATE = "￥%.2f X %d %s";
    private final List<ICartGoodsData> cart_data;

    public OrderGoodsDataAdapter(LayoutInflater inflater,
                                 final List<ICartGoodsData> items) {
        super(inflater, R.layout.order_goods_data_list_item);
        cart_data = items;
        setItems(cart_data);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_good_name, R.id.tv_unit_price, R.id.tv_good_total, R.id.tv_plus};
    }

    @Override
    protected void update(int position, ICartGoodsData item) {
        setText(0, item.get_name());
        setText(1, String.format(FORMAT_TOTAL_CALCULATE, item.get_price(), item.get_amount(), item.get_unit()));
        setText(2, String.format(FORMAT_PRICE, item.get_amount() * item.get_price()));
        setText(3, "备注:" + (item.get_plus() == null || "".equals(item.get_plus()) ? "无" : item.get_plus()));
    }
}
