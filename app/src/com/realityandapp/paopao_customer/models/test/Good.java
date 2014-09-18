package com.realityandapp.paopao_customer.models.test;

import com.realityandapp.paopao_customer.models.interfaces.IGood;
import com.realityandapp.paopao_customer.models.interfaces.IShop;

/**
 * Created by dd on 14-9-18.
 */
public class Good implements IGood {
    private static int i = 0;
    private String unit;
    private float price;
    private String _id;
    private String name;
    private String description;
    private String image;

    public Good() {
        i++;
        _id = String.valueOf(i);
        name = "good" + _id;
        description = "good description" + _id;
        price = i;
        unit = "份";
        image = "http://meishipaopao.dev.realityandapp.com/assets/noface_android.png";
    }

    @Override
    public String get_name() {
        return name;
    }

    @Override
    public String get_description() {
        return description;
    }

    @Override
    public float get_price() {
        return price;
    }

    @Override
    public String get_unit() {
        return unit;
    }

    @Override
    public String get_image() {
        return image;
    }

    @Override
    public String get_id() {
        return _id;
    }

}
