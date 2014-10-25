package com.realityandapp.paopao_customer.models.test;

import com.realityandapp.paopao_customer.models.interfaces.IAddress;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.networks.HttpApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 14-9-21.
 */
public class Address implements IAddress {
    private String _id;
    private List<Double> coordinates = new ArrayList<Double>();
    private String address;
    private String realname;
    private String phone;
    private String plus;

    public Address() {
//        i++;
//        _id = String.valueOf(i);
//        address = "地址" + _id;
//        realname = "姓名" + _id;
//        phone = String.format("133%08d", new Random().nextInt(100000000));
//        coordinates.add((new Random().nextDouble() - 0.5f) * 360 ); //lng
//        coordinates.add((new Random().nextDouble() - 0.5f) * 180); //lat
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
    public List<Double> get_coordinates() {
        return coordinates;
    }

    @Override
    public Double get_latitude() {
        return coordinates == null ? null : coordinates.get(2);
    }

    @Override
    public Double get_longitude() {
        return coordinates == null ? null : coordinates.get(1);
    }

    @Override
    public IAddress save() throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException {
        return DataProvider.save_address(this);
    }

    @Override
    public String get_id() {
        return _id;
    }

    public void set_coordinates(Double latitude, Double longitude) {
        System.out.println("latitude:" + latitude);
        System.out.println("longitude:" + longitude);
        List<Double> tmp = new ArrayList<Double>();
        tmp.add(longitude);
        tmp.add(latitude);
        this.coordinates = tmp;
    }

    @Override
    public void set_address(String address) {
        this.address = address;
    }

    @Override
    public void set_realname(String realname) {
        this.realname = realname;
    }

    @Override
    public void set_phone(String phone) {
        this.phone = phone;
    }

    @Override
    public void set_plus(String plus) {
        this.plus = plus;
    }

    @Override
    public boolean equals(Object o) {
        try{
            return ((IAddress) o).get_id().equals(get_id());
        }
        catch (Exception ex){
            return false;
        }
    }
}
