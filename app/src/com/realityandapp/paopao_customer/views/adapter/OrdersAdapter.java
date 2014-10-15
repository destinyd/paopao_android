package com.realityandapp.paopao_customer.views.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.interfaces.IOrder;
import com.realityandapp.paopao_customer.models.test.Order;
import com.realityandapp.paopao_customer.views.OrderActivity;
import com.realityandapp.paopao_customer.views.PayActivity;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class OrdersAdapter extends SingleTypeAdapter<IOrder> implements View.OnClickListener {

    private final List<IOrder> goods;
    private final Activity activity;

    public OrdersAdapter(Activity activity,
                         final List<IOrder> items) {
        super(activity, R.layout.orders_list_item);
        this.activity = activity;
        goods = items;
        setItems(goods);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{
                R.id.order_shop_name, R.id.order_status, R.id.order_desc, R.id.btn_action, R.id.rl_order
        };
    }

    @Override
    protected void update(int position, IOrder item) {
        setText(0, item.get_shop_name());
        setText(1, item.get_str_status());
        setText(2, String.format(Constants.Format.ORDER_DESC, item.get_total()));
        update_btn_action(item);
    }

    private void update_btn_action(IOrder item) {
        if(Order.OrderStatus.pending == item.get_status()){
            setText(3, "支付");
            getView(3, Button.class).setVisibility(View.VISIBLE);
        } else if(Order.OrderStatus.took_away == item.get_status()){
            setText(3, "收货");
            getView(3, Button.class).setVisibility(View.VISIBLE);
        }
        else{
            getView(3, Button.class).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer)v.getTag();
        IOrder order = getItem(position);
        switch (v.getId()) {
            case R.id.btn_action:
                action_for_order_status(order);
                break;
            case R.id.rl_order:
                go_to_order(order);
                break;
        }
    }

    private void go_to_order(IOrder order) {
        Intent intent = new Intent(activity, OrderActivity.class);
        intent.putExtra(Constants.Extra.ORDER, order);
        activity.startActivityForResult(intent, Constants.Request.ORDER);
    }

    private void action_for_order_status(IOrder order) {
        if("等待支付".equals( order.get_status())){
            go_to_pay(order);
        }
        else{
            // todo for other status
        }
    }

    private void go_to_pay(IOrder order) {
        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra(Constants.Extra.ORDER, order);
        activity.startActivityForResult(intent, Constants.Request.ORDER);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        bind_views(position);
        return view;
    }

    private void bind_views(int position) {
        Button button = getView(3, Button.class);
        button.setTag(position);
        button.setOnClickListener(this);

        RelativeLayout relativeLayout = getView(4, RelativeLayout.class);
        relativeLayout.setTag(position);
        relativeLayout.setOnClickListener(this);
    }
}
