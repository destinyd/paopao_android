package com.realityandapp.paopao_customer.models.test;

import com.realityandapp.paopao_customer.models.http.Good;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.models.interfaces.IGood;

import java.util.Random;

/**
 * Created by dd on 14-9-18.
 */
public class CartGoodsData implements ICartGoodsData {
    private static int i = 0;
    private IGood good;
    private int amount;
    private String _id;
    private String plus;

    public CartGoodsData() {
        i++;
        _id = String.valueOf(i);
        good = new Good();
        amount = 1 + new Random().nextInt(5);
        plus = "good plus" + _id;
    }

    public CartGoodsData(String good_id) {
        i++;
        plus = "";
        _id = String.valueOf(i);
        this.good = new Good(good_id);
        amount = 0;
    }


    @Override
    public String get_name() {
        return good.get_name();
    }

    @Override
    public String get_description() {
        return good.get_description();
    }

    @Override
    public float get_price() {
        return good.get_price();
    }

    @Override
    public String get_plus() {
        return plus;
    }

    @Override
    public void set_plus(String s) {
        plus = s;
    }

    @Override
    public void set_amount(int i) {
        amount = i;
    }

    @Override
    public String get_unit() {
        return good.get_unit();
    }

    @Override
    public int get_amount() {
        return amount;
    }

    @Override
    public String get_id() {
        return _id;
    }

    @Override
    public String get_good_id() {
        return good.get_id();
    }

}
