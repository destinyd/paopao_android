package com.realityandapp.paopao_customer.models.test;

import com.realityandapp.paopao_customer.models.interfaces.*;
import com.realityandapp.paopao_customer.networks.DataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dd on 14-9-23.
 */
public class Order implements IOrder {
    public static int i = 0;
    public String _id;
    public String status;
    public IShop shop;
    public IAddress address;
    public IDeliveryman deliveryman;
    public int shop_discount;
    public float shop_delivery_price;
    public float total = 0;
    public List<ICartGoodsData> goods = new ArrayList<ICartGoodsData>();

    public Order() {
        i++;
        shop = new Shop();
        shop_discount = 1 + new Random().nextInt(5000);
        shop_delivery_price = 5f;
        for(int i=0; i < 1 + new Random().nextInt(5); i++){
            CartGoodsData good = new CartGoodsData();
            goods.add(good);
            total += good.get_price() * good.get_amount();
        }
        total += shop_delivery_price;
        address = new Address();
        status = "等待支付";
        deliveryman = new Deliveryman();
    }

    public Order(IShopCart shop_cart) {
        shop = shop_cart.get_shop();
        shop_discount = shop_cart.get_shop_discount();
        shop_delivery_price = shop_cart.get_shop_delivery_price();
        goods = shop_cart.get_cart_goods();
        total = shop_cart.get_total();
        address = shop_cart.get_shop().get_address();
        status = "等待支付";
        deliveryman = new Deliveryman();
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
    public float get_delivery_price() {
        return shop_delivery_price;
    }

    @Override
    public int get_delivery_type() {
        return 0;
    }

    @Override
    public IAddress get_address() {
        return address;
    }

    @Override
    public float get_total() {
        return total;
    }

    @Override
    public String get_status() {
        return status;
    }

    @Override
    public IDeliveryman get_deliveryman() {
        return deliveryman;
    }

    @Override
    public void destroy() {
        DataProvider.destroy_order(get_id());
    }

    @Override
    public void set_address(IAddress address) {
        this.address = address;
    }

    @Override
    public void save() {
        DataProvider.save_order(this);
    }

    @Override
    public String get_id() {
        return _id;
    }

    @Override
    public List<ICartGoodsData> get_goods_data() {
        return goods;
    }


}
