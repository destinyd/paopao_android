package com.realityandapp.paopao_customer.models.test;

import com.realityandapp.paopao_customer.models.interfaces.ICart;
import com.realityandapp.paopao_customer.models.interfaces.ICartData;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.models.interfaces.IGood;

import java.util.*;

/**
 * Created by dd on 14-9-18.
 */
public class Cart implements ICart {
    private static int i = 0;
    private Date updated_at;
    private String _id;
    private List<ICartData> data = new ArrayList<ICartData>();

    public Cart() {
        i++;
        _id = String.valueOf(i);
        updated_at = Calendar.getInstance().getTime();
        for(int i=0; i < 1 + new Random().nextInt(3); i++){
            data.add(new CartData());
        }
    }

    @Override
    public Date get_updated_at() {
        return updated_at;
    }

    @Override
    public List<ICartData> get_data() {
        return data;
    }

    @Override
    public int get_goods_type_count() {
        int count = 0;
        for(ICartData d : data){
            count += d.get_shop_goods_data().size();
        }
        return count;
    }

    @Override
    public float get_total() {
        float total = 0;
        for(ICartData cart_data : data){
            for(ICartGoodsData cart_good_data : cart_data.get_shop_goods_data()) {
                total += cart_good_data.get_price() * cart_good_data.get_amount();
            }
        }
        return total;
    }

    @Override
    public int get_amount_count() {
        int total = 0;
        for(ICartData cart_data : data){
            for(ICartGoodsData cart_good_data : cart_data.get_shop_goods_data()) {
                total += cart_good_data.get_amount();
            }
        }
        return total;
    }

    @Override
    public String get_id() {
        return _id;
    }
}
