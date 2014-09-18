package com.realityandapp.paopao_customer.models.test;

import com.realityandapp.paopao_customer.models.interfaces.IShop;

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
    public String coordinates;
    public String avatar;

    public Shop() {
        i++;
        name = "name" + String.valueOf(i);
        description = "description" + String.valueOf(i);
        contact = "contact" + String.valueOf(i);
        avatar = "http://meishipaopao.dev.realityandapp.com/assets/noface_android.png";
    }

    @Override
    public String get_id() {
        return String.valueOf(i);
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
    public String get_coordinates() {
        return coordinates;
    }
}
