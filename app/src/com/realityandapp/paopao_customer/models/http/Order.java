package com.realityandapp.paopao_customer.models.http;

import com.google.gson.*;
import com.realityandapp.paopao_customer.models.interfaces.*;
import com.realityandapp.paopao_customer.models.test.Address;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.networks.HttpApi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 14-9-23.
 */
public class Order implements IOrder {
    public static int i = 0;
    public String _id;
    public OrderStatus status;
    public Shop shop;
    public Address address;
    public Deliveryman deliveryman;
    public int distance;
    public float delivery_price;
    public float human_total = 0f;
    public List<CartGoodsData> order_items = new ArrayList<CartGoodsData>();
    public String to_id;

    @Override
    public String get_shop_id() {
        return shop == null ? "" : shop.get_id();
    }

    @Override
    public String get_shop_name() {
        return shop == null ? "" : shop.get_name();
    }

    @Override
    public int get_distance() {
        return distance;
    }

    @Override
    public float get_delivery_price() {
        return delivery_price;
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
        return human_total;
    }

    @Override
    public OrderStatus get_status() {
        return status;
    }

    @Override
    public IDeliveryman get_deliveryman() {
        return deliveryman;
    }

    @Override
    public void destroy() throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        DataProvider.destroy_order(get_id());
    }

    @Override
    public void set_address(IAddress address) {
        this.address = (Address) address;
    }

    @Override
    public void save() throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        DataProvider.save_order(this);
    }

    @Override
    public String get_str_status() {
        if(status == OrderStatus.pending){
            return "等待支付";
        } else if(status == OrderStatus.paid){
            return "已支付"; //其实会直接跳过
        } else if(status == OrderStatus.system_accepted){
            return "等待取货";
        } else if(status == OrderStatus.took_away){
            return "配送中";
        } else if(status == OrderStatus.deliveried){
            return "已交货"; //跳过?
        } else if(status == OrderStatus.finished){
            return "配送成功";
        } else if(status == OrderStatus.accepted){
            return "已接受";
        } else if(status == OrderStatus.cancel){
            return "已取消";
        } else if(status == OrderStatus.expired){
            return "已过期";
        }
        return null;
    }

    @Override
    public void set_to_id(String to_id) {
        this.to_id = to_id;
    }

    @Override
    public String get_to_id() {
        return to_id;
    }

    @Override
    public String get_id() {
        return _id;
    }

    @Override
    public List<CartGoodsData> get_order_items() {
        return order_items;
    }

    public enum OrderStatus implements IOrderStatus{
        pending,
        paid,
        finished,
        cancel,
        accepted,
        system_accepted,
        took_away,
        deliveried,
        expired
    }

    public static class OrderSerializer implements JsonSerializer<Order> {
        public JsonElement serialize(final Order order, final Type type, final JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add("_id", new JsonPrimitive(order.get_id()));
            if(order.get_address() != null)
                result.add("address_id", new JsonPrimitive(order.get_address().get_id()));
            if(order.get_to_id() != null)
                result.add("to_id", new JsonPrimitive(order.get_to_id()));
            final JsonArray cart_goods_data = new JsonArray();
            for (final ICartGoodsData data : order.get_order_items()) {
                JsonObject obj_cart_goods_data = new JsonObject();
                if (data.get_id() != null)
                    obj_cart_goods_data.add("_id", new JsonPrimitive(data.get_id()));
                obj_cart_goods_data.add("plus", new JsonPrimitive(data.get_plus()));
                cart_goods_data.add(obj_cart_goods_data);
            }

            result.add("order_items_attributes", cart_goods_data);
            return result;
        }
    }
}
