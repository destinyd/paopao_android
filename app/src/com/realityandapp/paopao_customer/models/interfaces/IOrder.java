package com.realityandapp.paopao_customer.models.interfaces;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public interface IOrder extends IBase {
    public String get_shop_id();
    public String get_shop_name();
    public int get_shop_discount();
    public List<ICartGoodsData> get_goods_data();
    public float get_delivery_price();
    public int get_delivery_type();
    public IAddress get_address();
    public float get_total();
    public IOrderStatus get_status();
    public IDeliveryman get_deliveryman();
    public void destroy();
    public void set_address(IAddress address);
    public void save();
    public String get_str_status();

    public interface IOrderStatus {
    }
}