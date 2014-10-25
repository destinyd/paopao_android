package com.realityandapp.paopao_customer.models.interfaces;

import com.realityandapp.paopao_customer.networks.HttpApi;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public interface IAddress extends IBase {
    public String get_address();
    public String get_realname();
    public String get_phone();
    public String get_plus();
    public List<Double> get_coordinates();
    public Double get_latitude();
    public Double get_longitude();
    public IAddress save() throws HttpApi.RequestDataErrorException, HttpApi.AuthErrorException, HttpApi.NetworkErrorException;

    void set_address(String address);

    void set_realname(String realname);

    void set_phone(String phone);

    void set_plus(String plus);
}