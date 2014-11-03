package com.realityandapp.paopao_customer.models.http;

import com.realityandapp.paopao_customer.models.interfaces.IUrlObject;

/**
 * Created by DD on 14-10-17.
 */
public class UrlObject implements IUrlObject {
    String url;
    @Override
    public String get_url() {
        return url;
    }
}
