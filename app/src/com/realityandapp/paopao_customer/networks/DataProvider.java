package com.realityandapp.paopao_customer.networks;


import com.realityandapp.paopao_customer.models.User;
import com.realityandapp.paopao_customer.models.http.Order;
import com.realityandapp.paopao_customer.models.http.Shop;
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

    public static void destroy_order(String id) {
        //todo destroy order
    }

    public static void create_address(IAddress address) {
        // todo create address
    }

    public static void save_order(IOrder order) {
        // todo save order
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
}
