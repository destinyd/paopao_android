package com.realityandapp.paopao_customer.models.test;

import com.realityandapp.paopao_customer.models.interfaces.ICartData;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.models.interfaces.IShop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dd on 14-9-18.
 */
public class CartData implements ICartData {
    public static int i = 0;
    public String _id;
    public Shop shop;
    public int shop_discount;
    public float shop_delivery_price;
    public List<ICartGoodsData> goods = new ArrayList<ICartGoodsData>();

    public CartData() {
        i++;
        shop = new Shop();
        shop_discount = 1 + new Random().nextInt(5000);
        for(int i=0; i < 1 + new Random().nextInt(5); i++){
            goods.add(new CartGoodsData());
        }
        shop_delivery_price = 5f;
    }

    @Override
    public String get_id() {
        return String.valueOf(i);
    }

    @Override
    public String get_shop_id() {
        return _id;
    }

    @Override
    public String get_shop_name() {
        return shop != null ? shop.get_name() : "";
    }

    @Override
    public int get_shop_discount() {
        return shop_discount;
    }

    @Override
    public List<ICartGoodsData> get_shop_goods_data() {
        return goods;
    }

    @Override
    public float get_shop_delivery_price() {
        return shop_delivery_price;
    }
}
