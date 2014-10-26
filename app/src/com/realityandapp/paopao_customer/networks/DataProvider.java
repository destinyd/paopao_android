package com.realityandapp.paopao_customer.networks;


import com.google.gson.JsonObject;
import com.realityandapp.paopao_customer.models.User;
import com.realityandapp.paopao_customer.models.http.ShopCart;
import com.realityandapp.paopao_customer.models.test.*;
import com.realityandapp.paopao_customer.models.interfaces.*;

import java.util.*;

public class DataProvider {

    public static List<IShop> get_shops() throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.shops();
    }

    public static List<IGood> get_goods(String shop_id) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.shop_goods(shop_id);
//        List<IGood> list = new ArrayList<IGood>();
//        for(int i=0; i< 10; i++) {
//            Good good = new Good();
//            list.add(good);
//        }
//        return list;
    }

    public static ICart get_cart() {
        return new Cart();
    }

    public static IAddress get_default_address() throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.default_address();
    }

    public static IOrder my_order(String s) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.my_order(s);
    }

    public static List<IAddress> get_addresses()  throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.my_addresses();
    }

    public static void destroy_order(String order_id) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        HttpApi.destroy_order(order_id);
    }

    public static IAddress save_address(IAddress address) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.save_address(address);
    }

    public static Boolean save_order(IOrder order) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.save_order(order);
    }

    public static IAddress set_default_address(String address_id) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.set_default_address(address_id);
    }

    public static List<IOrder> get_orders() throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.user_orders();
//        List<IOrder> orders = new ArrayList<IOrder>();
//        for(int i=0; i< 20; i++) {
//            Order order = new Order();
//            orders.add(order);
//        }
//        return orders;
    }

    public static void sign_out() {
        User.current().delete();
    }

    public static IOrder shop_cart_to_order(IShopCart shop_cart) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.shop_cart_to_order(shop_cart);
//        return new Order(shop_cart);
    }

    public static IShopCart get_cart(String shop_id) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.get_cart(shop_id);
    }

    public static IGood get_good(String good_id) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.good(good_id);
    }

    public static IShop get_shop(String shop_id) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.shop(shop_id);
    }

    public static JsonObject calculate_distance_and_pricing(String shop_id, String address_id) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.calculate_distance_and_pricing(shop_id, address_id);
    }

    public static IShopCart save_shop_cart(IShopCart shop_cart) throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.save_shop_cart(shop_cart);
    }

    public static User sign_up(String phone, String verify_code, String password, String name, String email) {
        return HttpApi.sign_up(phone, verify_code, password, name, email);
    }

    public static Integer get_verify_code(String phone) {
        return HttpApi.get_verify_code(phone);
    }
}
