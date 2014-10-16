package com.realityandapp.paopao_customer.models.test;

import com.realityandapp.paopao_customer.models.http.Shop;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.models.interfaces.IShop;
import com.realityandapp.paopao_customer.models.interfaces.IShopCart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dd on 14-9-18.
 */
public class ShopCart implements IShopCart {
    public static int i = 0;
    public String _id;
    public Shop shop;
    public int shop_discount;
    public float shop_delivery_price;
    public List<ICartGoodsData> goods = new ArrayList<ICartGoodsData>();

    public ShopCart() {
        i++;
        shop = new Shop();
        shop_discount = 1 + new Random().nextInt(5000);
//        for(int i=0; i < 1 + new Random().nextInt(5); i++){
//            goods.add(new CartGoodsData());
//        }
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
    public List<ICartGoodsData> get_cart_goods() {
        return goods;
    }

    @Override
    public float get_shop_delivery_price() {
        return shop_delivery_price;
    }

    @Override
    public IShop get_shop() {
        return shop;
    }

    @Override
    public void add_good(String good_id, int amount) {
        ICartGoodsData temp = null;
        temp = get_good_data_by_id(good_id);
        if(temp == null){
            temp = new CartGoodsData(good_id);
        }
        temp.set_amount(temp.get_amount() + amount);
        if(temp.get_amount() < 1){
            goods.remove(temp);
        }
        else if(!goods.contains(temp)){
            goods.add(temp);
        }
    }

    @Override
    public int get_good_amount(String id) {
        ICartGoodsData good_data = get_good_data_by_id(id);
        return good_data == null ? 0 : good_data.get_amount();
    }

    @Override
    public float get_goods_total() {
        float f = 0f;
        for(ICartGoodsData good : goods){
            f += good.get_amount() * good.get_price();
        }
        return f;
    }

    @Override
    public int get_goods_amount() {
        int f = 0;
        for(ICartGoodsData good : goods){
            f += good.get_amount();
        }
        return f;
    }

    @Override
    public float get_total() {
        return get_goods_total() + get_shop_delivery_price();
    }

    public ICartGoodsData get_good_data_by_id(String good_id){
        for(ICartGoodsData good : goods){
            if(good.get_good_id().equals(good_id)){
                return good;
            }
        }
        return null;
    }
}
