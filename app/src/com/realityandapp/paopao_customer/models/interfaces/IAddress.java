package com.realityandapp.paopao_customer.models.interfaces;

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
    public void save();
}