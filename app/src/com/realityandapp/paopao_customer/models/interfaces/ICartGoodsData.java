package com.realityandapp.paopao_customer.models.interfaces;

/**
 * Created by dd on 14-9-21.
 */
public interface ICartGoodsData extends IBase {
    public String get_name();
    public String get_description();
    public String get_unit();
    public int get_amount();
    public float get_price();
}
