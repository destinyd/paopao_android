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
    public String get_plus();
    public void set_plus(String s);
    public void set_amount(int i);
    public String get_good_id();
}
