package com.realityandapp.paopao_customer.models.http;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.models.interfaces.IShop;
import com.realityandapp.paopao_customer.models.interfaces.IShopCart;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.networks.HttpApi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dd on 14-9-18.
 */
public class ShopCart implements IShopCart {
//    @Expose
    public String _id;
//    @Expose
    public String shop_id;
    public IShop shop = null;
    public int shop_discount;
    public float shop_delivery_price;
//    @Expose
//    @SerializedName("cart_items_attributes")
    public List<CartGoodsData> cart_items = new ArrayList<CartGoodsData>();

    public ShopCart() {
        //todo server
//        shop_discount = 1 + new Random().nextInt(5000);
//        shop_delivery_price = 5f;
    }

    @Override
    public String get_id() {
        return _id;
    }

    @Override
    public String get_shop_id() {
        return shop_id;
    }

    @Override
    public String get_shop_name() {
        return shop != null ? shop.get_name() : "";
    }

    @Override
    public int get_shop_discount() {
        return shop_discount;
    }

//    @Override
    public List<CartGoodsData> get_cart_items() {
        return cart_items;
    }

    @Override
    public float get_shop_delivery_price() {
        return shop_delivery_price;
    }

    @Override
    public IShop get_shop() {
        if(shop != null)
            try {
                shop = DataProvider.get_shop(shop_id);
            } catch (HttpApi.RequestDataErrorException e) {
                e.printStackTrace();
            } catch (HttpApi.AuthErrorException e) {
                e.printStackTrace();
            } catch (HttpApi.NetworkErrorException e) {
                e.printStackTrace();
            }
        return shop;
    }

    @Override
    public void add_good(String good_id, int amount) {
        CartGoodsData temp = null;
        temp = get_good_data_by_id(good_id);
        if(temp == null){
            temp = new CartGoodsData(good_id);
        }
        temp.set_amount(temp.get_amount() + amount);
        if(temp.get_amount() < 1){
            cart_items.remove(temp);
        }
        else if(!cart_items.contains(temp)){
            cart_items.add(temp);
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
        for(ICartGoodsData good : cart_items){
            f += good.get_amount() * good.get_price();
        }
        return f;
    }

    @Override
    public int get_goods_amount() {
        int f = 0;
        for(ICartGoodsData good : cart_items){
            f += good.get_amount();
        }
        return f;
    }

    @Override
    public float get_total() {
        return get_goods_total() + get_shop_delivery_price();
    }

    public CartGoodsData get_good_data_by_id(String good_id){
        for(CartGoodsData good : cart_items){
            if(good.get_good_id().equals(good_id)){
                return good;
            }
        }
        return null;
    }

    public static class ShopCartSerializer implements JsonSerializer<ShopCart> {
        public JsonElement serialize(final ShopCart shop_cart, final Type type, final JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add("_id", new JsonPrimitive(shop_cart.get_id()));
            result.add("shop_id", new JsonPrimitive(shop_cart.get_shop_id()));
            final JsonArray cart_goods_data = new JsonArray();
            for(final ICartGoodsData data : shop_cart.get_cart_items()){
                JsonObject obj_cart_goods_data = new JsonObject();
                if(data.get_id() != null)
                    obj_cart_goods_data.add("_id", new JsonPrimitive(data.get_id()));
                obj_cart_goods_data.add("amount", new JsonPrimitive(data.get_amount()));
                obj_cart_goods_data.add("good_id", new JsonPrimitive(data.get_good_id()));
                obj_cart_goods_data.add("plus", new JsonPrimitive(data.get_plus()));
                cart_goods_data.add(obj_cart_goods_data);
            }
            
            result.add("cart_items_attributes", cart_goods_data);// shop_cart.get_cart_items())
            return result;
        }
    }
}
