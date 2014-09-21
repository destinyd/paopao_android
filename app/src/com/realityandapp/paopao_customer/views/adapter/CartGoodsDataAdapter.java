package com.realityandapp.paopao_customer.views.adapter;

import android.view.LayoutInflater;
import android.widget.ListView;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.ICartData;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class CartGoodsDataAdapter extends SingleTypeAdapter<ICartGoodsData> {

    private static final String FORMAT_UNIT_PRICE = "￥%.2f/%s";
    private final List<ICartGoodsData> cart_data;

    public CartGoodsDataAdapter(LayoutInflater inflater,
                                final List<ICartGoodsData> items) {
        super(inflater, R.layout.cart_goods_data_list_item);
        cart_data = items;
        setItems(cart_data);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.cb_good, R.id.tv_amount, R.id.tv_unit_price };
    }

    @Override
    protected void update(int position, ICartGoodsData item) {
        setText(0, item.get_name());
        setText(1, String.valueOf(item.get_amount()));
        setText(2, String.format(FORMAT_UNIT_PRICE, item.get_price(), item.get_unit()));
    }
}
