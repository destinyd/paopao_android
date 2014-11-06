package com.realityandapp.paopao_customer.models.http;

import com.realityandapp.paopao_customer.models.interfaces.IUploader;
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
        return thumb != null && !"/noface_thumb.png".equals(thumb.get_url()) ? thumb.get_url() : "/assets/noface_thumb.png";
    }

    @Override
    public String get_android() {
        return android != null && !"/noface_android.png".equals(android.get_url()) ? android.get_url() : "/assets/noface_android.png";
    }

    @Override
    public String get_show() {
        return android != null && !"/noface_show.png".equals(show.get_url()) ? show.get_url() : "/assets/noface_show.png";
    }
}
