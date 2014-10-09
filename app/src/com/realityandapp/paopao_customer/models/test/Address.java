package com.realityandapp.paopao_customer.models.test;

import com.realityandapp.paopao_customer.models.interfaces.IAddress;
import com.realityandapp.paopao_customer.networks.DataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dd on 14-9-21.
 */
public class Address implements IAddress {
    private static int i = 0;
    private String _id;
    private List<Float> coordinates = new ArrayList<Float>();
    private String address;
    private String realname;
    private String phone;
    private String plus;

    public Address() {
        i++;
        _id = String.valueOf(i);
        address = "地址" + _id;
        realname = "姓名" + _id;
        phone = String.format("133%08d", new Random().nextInt(100000000));
        coordinates.add((new Random().nextFloat() - 0.5f) * 360 ); //lng
        coordinates.add((new Random().nextFloat() - 0.5f) * 180); //lat
    }

    @Override
    public String get_address() {
        return address;
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
    public String get_plus() {
        return plus;
    }

    @Override
    public List<Float> get_coordinates() {
        return coordinates;
    }

    @Override
    public Float get_lat() {
        return coordinates == null ? null : coordinates.get(1);
    }

    @Override
    public Float get_lng() {
        return coordinates == null ? null : coordinates.get(2);
    }

    @Override
    public void save() {
        DataProvider.create_address(this);
    }

    @Override
    public String get_id() {
        return _id;
    }
}
