package com.realityandapp.paopao_customer.models.interfaces;

import java.io.Serializable;

/**
 * Created by dd on 14-9-18.
 */
public interface IShop extends Serializable {
    public String get_id();
    public String get_name();
    public String get_address();
    public String get_description();
    public String get_contact();
    public String get_coordinates();
    public boolean is_shop_delivery();
    public String get_avatar();
}