package com.realityandapp.paopao_customer.networks;


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

    public static Cart get_cart() {
        return new Cart();
    }
}
