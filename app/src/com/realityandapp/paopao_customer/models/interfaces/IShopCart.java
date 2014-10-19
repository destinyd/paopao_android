package com.realityandapp.paopao_customer.models.interfaces;

import com.realityandapp.paopao_customer.networks.HttpApi;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public interface IShopCart extends IBase {
    public String get_shop_id();
    public String get_shop_name();
    public int get_shop_discount();
//    public List<ICartGoodsData> get_cart_items();
    public float get_shop_delivery_price();
    public IShop get_shop();
    public void add_good(String good_id, int amount);
    public int get_good_amount(String id);
    public float get_goods_total();
    public int get_goods_amount();
    public float get_total();
}