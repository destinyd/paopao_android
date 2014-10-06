package com.realityandapp.paopao_customer.views.adapter;

import android.view.LayoutInflater;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IGood;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class GoodsGridAdapter extends SingleTypeAdapter<IGood> {

    private final List<IGood> goods;
    private final String str_format_desc = "%.2f / %s";

    public GoodsGridAdapter(LayoutInflater inflater,
                            final List<IGood> items) {
        super(inflater, R.layout.goods_grid_list_item);
        goods = items;
        setItems(goods);
    }

//    @Override
//    public long getItemId(final int position) {
//        final String id = getItem(position).get_id();
//        return !TextUtils.isEmpty(id) ? id.hashCode() : super
//                .getItemId(position);
//    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.iv_good_icon, R.id.good_title, R.id.good_desc };
    }

    @Override
    protected void update(int position, IGood item) {
        ImageLoader.getInstance().displayImage(item.get_image(), imageView(0));
        setText(1, item.get_name());
        setText(2, String.format(str_format_desc, item.get_price(), item.get_unit()));
//        setText(0, joke.getName());
//        setText(1, PrettyDateFormat.defaultFormat(joke.getCreated_at()));
//        setText(2, joke.getText());
    }
}
