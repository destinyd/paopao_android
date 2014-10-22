package com.realityandapp.paopao_customer.models.http;

import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.models.interfaces.IGood;

/**
 * Created by dd on 14-9-18.
 */
public class CartGoodsData implements ICartGoodsData {
    private String good_id;
//    private Good good;

    private int amount = 0;

    private Float human_price;
    private String good_name = "";
    private String good_unit = "";

    private String _id;
    private String plus = "";

    public CartGoodsData() {
    }

    public CartGoodsData(IGood good) {
        good_id = good.get_id();
        good_name = good.get_name();
        good_unit = good.get_unit();
        human_price = good.get_price();
    }


    @Override
    public String get_good_name() {
        return good_name;
    }

    @Override
    public String get_good_unit() {
        return good_unit;
    }

    @Override
    public Float get_price() {
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

//    @Override
//    public IGood get_good() {
//        if(good == null){
//            try {
//                good = (Good) DataProvider.get_good(good_id);
//            } catch (HttpApi.RequestDataErrorException e) {
//                e.printStackTrace();
//            } catch (HttpApi.AuthErrorException e) {
//                e.printStackTrace();
//            } catch (HttpApi.NetworkErrorException e) {
//                e.printStackTrace();
//            }
//        }
//        return good;
//    }
}
