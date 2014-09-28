package com.realityandapp.paopao_customer;

/**
 * Created by DD on 14-9-27.
 */
public class Constants {
    public static class Extra{
        public static final String SHOP = "shop";
        public static final String STEP = "step";
        public static final String ORDER = "order";
    }

    public static class Request {
        public static final int ORDER = 11;
        public static final int ADDRESS = 12;
    }
    
    public static class Format{
        public static final String FORMAT_PRICE = "￥%.2f";
        public static final String FORMAT_CONTACT = "%s(%s)";
        public static final String FORMAT_FULL_CONTACT = "%s %s(%s)";
        public static final String FORMAT_FULL_CONTACT_TOAST = "%s(%s)\n%s";
    }
}
