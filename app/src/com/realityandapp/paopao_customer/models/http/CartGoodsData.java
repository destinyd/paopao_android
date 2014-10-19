package com.realityandapp.paopao_customer.models.http;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.realityandapp.paopao_customer.models.http.Good;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.models.interfaces.IGood;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.networks.HttpApi;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

/**
 * Created by dd on 14-9-18.
 */
public class CartGoodsData implements ICartGoodsData {
    private String good_id;
    private IGood good;

    private int amount = 0;

    private float human_price = -1f;

    private String _id;
    private String plus = "";

    public CartGoodsData() {
    }

    public CartGoodsData(String good_id) {
        this.good_id = good_id;
    }


    @Override
    public String get_name() {
        return get_good().get_name();
    }

    @Override
    public String get_description() {
        return get_good().get_description();
    }

    @Override
    public float get_price() {
        if(human_price == -1f)
            return get_good().get_price();
        return human_price;
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
        return get_good().get_unit();
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
        return good_id;
    }

    @Override
    public IGood get_good() {
        if(good == null){
            try {
                good = DataProvider.get_good(good_id);
            } catch (HttpApi.RequestDataErrorException e) {
                e.printStackTrace();
            } catch (HttpApi.AuthErrorException e) {
                e.printStackTrace();
            } catch (HttpApi.NetworkErrorException e) {
                e.printStackTrace();
            }
        }
        return good;
    }
}
