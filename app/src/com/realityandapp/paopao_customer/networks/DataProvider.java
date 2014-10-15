package com.realityandapp.paopao_customer.networks;


import com.realityandapp.paopao_customer.models.http.Order;
import com.realityandapp.paopao_customer.models.test.*;
import com.realityandapp.paopao_customer.models.interfaces.*;

import java.util.*;

public class DataProvider {

    public static List<IShop> get_shops(){//} throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
//        return HttpApi.get_topic_list();
        List<IShop> list = new ArrayList<IShop>();
        for(int i=0; i< 10; i++) {
            Shop shop = new Shop();
            list.add(shop);
        }
        return list;
    }

    public static List<IGood> get_goods(String id) {
        List<IGood> list = new ArrayList<IGood>();
        for(int i=0; i< 10; i++) {
            Good good = new Good();
            list.add(good);
        }
        return list;
    }

    public static ICart get_cart() {
        return new Cart();
    }

    public static IAddress get_default_address() {
        return new Address();
    }

    public static IOrder get_order(String s) {
        return new Order();
    }

    public static List<IAddress> get_addresses() {
        List<IAddress> addresses = new ArrayList<IAddress>();
        for(int i=0; i< 10; i++) {
            Address good = new Address();
            addresses.add(good);
        }
        return addresses;
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

    public static void set_default_address(IAddress address) {
        // todo set default address
    }

    public static List<IOrder> get_orders() throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return HttpApi.get_my_orders();
//        List<IOrder> orders = new ArrayList<IOrder>();
//        for(int i=0; i< 20; i++) {
//            Order order = new Order();
//            orders.add(order);
//        }
//        return orders;
    }

    public static void sign_out() {
        // todo sign out
    }

    public static IOrder shop_cart_to_order(IShopCart shop_cart) {
        // todo submit cart and get order
        return new Order(shop_cart);
    }
}
