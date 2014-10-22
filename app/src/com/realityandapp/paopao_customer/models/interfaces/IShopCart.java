package com.realityandapp.paopao_customer.models.interfaces;

import com.realityandapp.paopao_customer.networks.HttpApi;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public interface IShopCart extends IBase {
    public String get_shop_id();
    public String get_shop_name();
    public Integer get_distance();
//    public List<ICartGoodsData> get_cart_items();
    public Float get_delivery_price();
    public IShop get_shop();
    public void add_good(IGood good, int amount);
    public int get_good_amount(String id);
    public float get_goods_total();
    public int get_goods_amount();
    public float get_total();
    public Integer calculate_distance_and_pricing(String shop_id, String address_id) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException;
    public void set_to_id(String address_id);
    public String get_to_id();
}