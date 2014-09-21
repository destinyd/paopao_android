package com.realityandapp.paopao_customer.models.test;

import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.models.interfaces.IGood;

import java.util.Random;

/**
 * Created by dd on 14-9-18.
 */
public class CartGoodsData implements ICartGoodsData {
    private static int i = 0;
    private int amount;
    private String unit;
    private float price;
    private String _id;
    private String name;
    private String description;

    public CartGoodsData() {
        i++;
        _id = String.valueOf(i);
        name = "good" + _id;
        description = "good description" + _id;
        price = 1 + new Random().nextInt(20);
        unit = "份";
        amount = 1 + new Random().nextInt(3);
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
    public int get_amount() {
        return amount;
    }

    @Override
    public String get_id() {
        return _id;
    }

}