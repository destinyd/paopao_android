package com.realityandapp.paopao_customer.models.http;

import com.realityandapp.paopao_customer.models.interfaces.IShop;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class Shop implements IShop {
    public static int i = 0;
    public String _id;
    public String name;
    public String address;
    public String description;
    public String contact;
    public boolean is_shop_delivery;
    public List<Float> coordinates;
    // todo to avatar class
    public Object avatar;

//    public Shop() {
//        i++;
//        name = "shop" + String.valueOf(i);
//        description = "shop description" + String.valueOf(i);
//        contact = "shop contact" + String.valueOf(i);
//        avatar = "http://meishipaopao.dev.realityandapp.com/assets/noface_android.png";
////        address = new Address();
//    }

    @Override
    public String get_id() {
        return _id;
    }

    @Override
    public String get_name() {
        return name;
    }

    @Override
    public String get_address() {
        return address;
    }

    @Override
    public String get_description() {
        return description;
    }

    @Override
    public String get_contact() {
        return contact;
    }

    @Override
    public boolean is_shop_delivery() {
        return is_shop_delivery;
    }

    @Override
    public String get_avatar() {
        return "http://meishipaopao.dev.realityandapp.com/assets/noface_android.png";
    }

    @Override
    public List<Float> get_coordinates() {
        return coordinates;
    }
}
