package com.realityandapp.paopao_customer.models.test;

import com.realityandapp.paopao_customer.models.interfaces.IAddress;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.models.interfaces.IOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dd on 14-9-23.
 */
public class Order implements IOrder {
    public static int i = 0;
    public String _id;
    public Shop shop;
    public Address address;
    public int shop_discount;
    public float shop_delivery_price;
    public List<ICartGoodsData> goods = new ArrayList<ICartGoodsData>();

    public Order() {
        i++;
        shop = new Shop();
        shop_discount = 1 + new Random().nextInt(5000);
        for(int i=0; i < 1 + new Random().nextInt(5); i++){
            goods.add(new CartGoodsData());
        }
        shop_delivery_price = 5f;
    }

    @Override
    public String get_shop_id() {
        return shop.get_id();
    }

    @Override
    public String get_shop_name() {
        return shop.get_name();
    }

    @Override
    public int get_shop_discount() {
        return shop_discount;
    }

    @Override
    public float get_order_delivery_price() {
        return shop_delivery_price;
    }

    @Override
    public int get_order_delivery_type() {
        return 0;
    }

    @Override
    public IAddress get_order_address() {
        return address;
    }

    @Override
    public String get_id() {
        return _id;
    }

    @Override
    public List<ICartGoodsData> get_order_goods_data() {
        return goods;
    }
}
