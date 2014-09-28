package com.realityandapp.paopao_customer.views.adapter;

import android.view.LayoutInflater;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IAddress;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class AddressesAdapter extends SingleTypeAdapter<IAddress> {

    private final List<IAddress> addresses;
    private IAddress address_default;

    public AddressesAdapter(LayoutInflater inflater,
                            final List<IAddress> items, final IAddress address_default) {
        super(inflater, R.layout.address_list_item);
        addresses = items;
        setItems(addresses);
        this.address_default = address_default;
    }

//    @Override
//    public long getItemId(final int position) {
//        final String id = getItem(position).get_id();
//        return !TextUtils.isEmpty(id) ? id.hashCode() : super
//                .getItemId(position);
//    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.rb_check, R.id.tv_contact, R.id.tv_address};
    }

    @Override
    protected void update(int position, IAddress item) {
        setChecked(0, address_default == item);
        setText(1, String.format(Constants.Format.FORMAT_CONTACT, item.get_realname(), item.get_phone()));
        setText(2, item.get_address());
    }

    public IAddress get_address_default() {
        return address_default;
    }

    public void set_address_default(IAddress address_default) {
        this.address_default = address_default;
    }
}
