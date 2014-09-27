package com.realityandapp.paopao_customer.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.ICartData;
import com.realityandapp.paopao_customer.models.interfaces.IShop;
import com.realityandapp.paopao_customer.utils.ListViewUtils;
import com.realityandapp.paopao_customer.views.ShopGoodsActivity;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class CartDataAdapter extends SingleTypeAdapter<ICartData> implements View.OnClickListener {
    private final LayoutInflater inflater;
    private final List<ICartData> cart_data;
    private final Context context;

    public CartDataAdapter(LayoutInflater inflater,
                           final List<ICartData> items) {
        super(inflater, R.layout.cart_data_list_item);
        this.inflater = inflater;
        context = inflater.getContext();
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
        TextView view = getView(1, TextView.class);
        view.setTag(cart_data.get(position).get_shop());
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_goto_shop:
                Intent intent = new Intent(inflater.getContext(), ShopGoodsActivity.class);
                intent.putExtra(Constants.Extra.SHOP, (IShop)view.getTag());
                context.startActivity(intent);
                break;
        }
    }
}
