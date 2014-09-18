package com.realityandapp.paopao_customer.views.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IShop;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class ShopsAdapter extends SingleTypeAdapter<IShop> {

    private final List<IShop> shops;

    public ShopsAdapter(LayoutInflater inflater,
                                        final List<IShop> items) {
        super(inflater, R.layout.shops_list_item);
        shops = items;
        setItems(shops);
    }

//    @Override
//    public long getItemId(final int position) {
//        final String id = getItem(position).get_id();
//        return !TextUtils.isEmpty(id) ? id.hashCode() : super
//                .getItemId(position);
//    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.iv_shop_icon, R.id.topic_title, R.id.topic_desc };
    }

    @Override
    protected void update(int position, IShop item) {
        ImageLoader.getInstance().displayImage(item.get_avatar(), imageView(0));
        setText(1, item.get_name());
        setText(2, item.get_description());
//        setText(0, joke.getName());
//        setText(1, PrettyDateFormat.defaultFormat(joke.getCreated_at()));
//        setText(2, joke.getText());
    }
}
