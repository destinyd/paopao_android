package com.realityandapp.paopao_customer.models.http;

import com.realityandapp.paopao_customer.models.interfaces.IDeliveryman;

/**
 * Created by dd on 14-9-18.
 */
public class Deliveryman implements IDeliveryman {
    private static int i = 0;
    private String id;
    private String realname;
    private String phone;
    private String weixinid;

    public Deliveryman() {
        i++;
        id = String.valueOf(i);
        realname = "deliveryman " + id;
        phone = "13333333333";
        weixinid = "test";
    }

    @Override
    public String get_id() {
        return id;
    }

    @Override
    public String get_realname() {
        return realname;
    }

    @Override
    public String get_phone() {
        return phone;
    }

    @Override
    public String get_weixinid() {
        return weixinid;
    }
}
