package com.realityandapp.paopao_customer.networks;


import com.realityandapp.paopao_customer.models.test.Shop;
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
}
