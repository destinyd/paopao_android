package com.realityandapp.paopao_customer.models.http;

import com.realityandapp.paopao_customer.networks.HttpApi;

/**
 * Created by DD on 14-10-17.
 */
public class Uploader implements IUploader {
    String url;
    UrlObject thumb;
    UrlObject android;
    UrlObject show;

    public String get_default() {
        return HttpApi.SITE + get_android();
    }

    @Override
    public String get_url() {
        return url;
    }

    @Override
    public String get_thumb() {
        return thumb == null ? "/assets/noface_thumb.png" : thumb.get_url();
    }

    @Override
    public String get_android() {
        return android == null ? "/assets/noface_android.png" : android.get_url();
    }

    @Override
    public String get_show() {
        return show == null ? "/assets/noface_show.png" : show.get_url();
    }
}
