package com.realityandapp.paopao_customer.models.interfaces;

import com.realityandapp.paopao_customer.models.http.CartGoodsData;
import com.realityandapp.paopao_customer.networks.HttpApi;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public interface IOrder extends IBase {
    public String get_shop_id();
    public String get_shop_name();
    public int get_distance();
    public List<CartGoodsData> get_order_items();
    public float get_delivery_price();
    public int get_delivery_type();
    public IAddress get_address();
    public float get_total();
    public IOrderStatus get_status();
    public IDeliveryman get_deliveryman();
    public void destroy() throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException;
    public void set_address(IAddress address);
    public void save() throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException;
    public String get_str_status();
    public void set_to_id(String to_id);
    public String get_to_id();

    public interface IOrderStatus {
    }
}