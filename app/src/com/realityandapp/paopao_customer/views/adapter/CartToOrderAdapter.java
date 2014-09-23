package com.realityandapp.paopao_customer.views.adapter;

import android.view.LayoutInflater;
import android.widget.ListView;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.ICartData;
import com.realityandapp.paopao_customer.utils.ListViewUtils;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class CartToOrderAdapter extends SingleTypeAdapter<ICartData> {
    private static final String FORMAT_PRICE = "￥%.2f";
    private final LayoutInflater inflater;
    private final List<ICartData> cart_data;

    public CartToOrderAdapter(LayoutInflater inflater,
                              final List<ICartData> items) {
        super(inflater, R.layout.cart_to_order_list_item);
        this.inflater = inflater;
        cart_data = items;
        setItems(cart_data);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.tv_shop_name, R.id.tv_distance, R.id.lv_cart_to_order_data, R.id.tv_delivery_price };
    }

    @Override
    protected void update(int position, ICartData item) {
        setText(0, item.get_shop_name());
        setText(1, item.get_shop_discount() + "米");
        ListView listView = getView(2, ListView.class);
        listView.setAdapter(new CartToOrderGoodsDataAdapter(inflater, item.get_shop_goods_data()));
        ListViewUtils.setListViewHeightBasedOnChildren(listView);
        setText(3, String.format(FORMAT_PRICE, item.get_shop_delivery_price()));
    }
}
