package com.realityandapp.paopao_customer.models.http;

import com.realityandapp.paopao_customer.models.interfaces.IGood;

/**
 * Created by dd on 14-9-18.
 */
public class Good implements IGood {
    private static int i = 0;
    private String unit;
//    private float price;
    private float human_price;
    private String id;
    private String name;
    private String description;
    private Uploader image;

    public Good() {
        i++;
        id = String.valueOf(i);
        name = "good" + id;
        description = "good description" + id;
//        price = i;
        unit = "份";
//        image = "http://meishipaopao.dev.realityandapp.com/assets/noface_android.png";
    }

    public Good(String id) {
        i++;
        id = id;
        name = "good" + id;
        description = "good description" + id;
//        price = i;
        unit = "份";
//        image = "http://meishipaopao.dev.realityandapp.com/assets/noface_android.png";
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
        return human_price;
    }

    @Override
    public String get_unit() {
        return unit;
    }

    @Override
    public Object get_image() {
        return image.get_default();
    }

    @Override
    public String get_id() {
        return id;
    }

}
