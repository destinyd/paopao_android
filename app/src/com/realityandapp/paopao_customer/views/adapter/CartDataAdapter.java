package com.realityandapp.paopao_customer.views.adapter;

import android.view.LayoutInflater;
import android.widget.ListView;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.ICartData;
import com.realityandapp.paopao_customer.utils.ListViewUtils;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class CartDataAdapter extends SingleTypeAdapter<ICartData> {
    private final LayoutInflater inflater;
    private final List<ICartData> cart_data;

    public CartDataAdapter(LayoutInflater inflater,
                           final List<ICartData> items) {
        super(inflater, R.layout.cart_data_list_item);
        this.inflater = inflater;
        cart_data = items;
        setItems(cart_data);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.cb_shop_goods, R.id.tv_goto_shop, R.id.tv_shop_name, R.id.iv_cart_goods_data };
    }

    @Override
    protected void update(int position, ICartData item) {
        setText(2, item.get_shop_name());
        ListView listView = getView(3, ListView.class);
        listView.setAdapter(new CartGoodsDataAdapter(inflater, item.get_shop_goods_data()));
        ListViewUtils.setListViewHeightBasedOnChildren(listView);
    }
}
