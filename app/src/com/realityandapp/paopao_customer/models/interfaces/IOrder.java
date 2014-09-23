package com.realityandapp.paopao_customer.models.interfaces;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public interface IOrder extends IBase {
    public String get_shop_id();
    public String get_shop_name();
    public int get_shop_discount();
    public List<ICartGoodsData> get_order_goods_data();
    public float get_order_delivery_price();
    public int get_order_delivery_type();
    public IAddress get_order_address();
}