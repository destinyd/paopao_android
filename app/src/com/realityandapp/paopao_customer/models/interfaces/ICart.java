package com.realityandapp.paopao_customer.models.interfaces;

import java.util.Date;
import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public interface ICart extends IBase {
    public Date get_updated_at();
    public List<ICartData> get_data();
    public int get_goods_type_count();
    public float get_total();
    public int get_amount_count();
}