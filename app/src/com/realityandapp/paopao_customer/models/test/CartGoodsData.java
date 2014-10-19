package com.realityandapp.paopao_customer.models.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.realityandapp.paopao_customer.models.http.Good;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.models.interfaces.IGood;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.networks.HttpApi;

import java.util.Random;

/**
 * Created by dd on 14-9-18.
 */
public class CartGoodsData implements ICartGoodsData {
    private static int i = 0;
    @Expose
    private String good_id;
    @Expose(serialize=false)
    private IGood good;

    @Expose
    private int amount;

    @Expose
    private String _id;
    @Expose
    private String plus;

    public CartGoodsData() {
        i++;
        _id = String.valueOf(i);
        good = new Good();
        amount = 1 + new Random().nextInt(5);
        plus = "good plus" + _id;
    }

    public CartGoodsData(String good_id) {
        plus = "";
        this.good_id = good_id;
        amount = 0;
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
        return get_good().get_price();
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
