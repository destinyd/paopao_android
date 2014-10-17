package com.realityandapp.paopao_customer.networks;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mindpin.android.authenticator.RequestResult;
import com.realityandapp.paopao_customer.PaopaoCustomerApplication;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.controllers.AuthenticatorsController;
import com.realityandapp.paopao_customer.models.interfaces.IGood;
import com.realityandapp.paopao_customer.models.interfaces.IOrder;
import com.realityandapp.paopao_customer.models.http.Order;
import com.realityandapp.paopao_customer.models.interfaces.IShop;
import com.realityandapp.paopao_customer.models.http.Shop;
import com.realityandapp.paopao_customer.models.http.Good;
import com.realityandapp.paopao_customer.models.interfaces.IShopCart;
import com.realityandapp.paopao_customer.models.test.ShopCart;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by fushang318 on 2014/8/11.
 */
public class HttpApi {
    /**
     * http api url begin
     */
    public static final String SITE = PaopaoCustomerApplication.get_context().getResources().getString(R.string.http_site);
    public static final String USER_SITE = PaopaoCustomerApplication.get_context().getResources().getString(R.string.user_http_site);
    public static final String DELIVERY_SITE = PaopaoCustomerApplication.get_context().getResources().getString(R.string.deliveryman_http_site);
    public static final String TRADER_SITE = PaopaoCustomerApplication.get_context().getResources().getString(R.string.trader_http_site);

    public static final String USER_ORDERS = USER_SITE + "/orders.json";
    public static final String FORMAT_USER_ORDER = USER_SITE + "/orders/%s.json";
    public static final String SHOPS = SITE + "/shops.json";
    public static final String FORMAT_SHOP_GOODS = SITE + "/shops/%s/goods.json";
    private static final String FORMAT_SHOP_CART = SITE + "/shops/%s/cart.json";

    /**
     * http api url end
     */


    public static List<IOrder> user_orders() throws AuthErrorException, RequestDataErrorException, NetworkErrorException {
        return new RequestProcess<List<IOrder>>(){

            @Override
            public List<IOrder> call(RequestResult rr) {
                System.out.println("body:" + rr.body);
                Type collectionType = new TypeToken<List<Order>>(){}.getType();
                Gson gson = new Gson();
                return gson.fromJson(rr.body, collectionType);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                return auth.get_http_request(USER_ORDERS, "GET");
            }
        }.request();
    }

    public static IOrder my_order(final String id) throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        System.out.println("order id:" + id);
        return new RequestProcess<IOrder>(){

            @Override
            public IOrder call(RequestResult rr) {
                System.out.println("body:" + rr.body);
                Gson gson = new Gson();
                return gson.fromJson(rr.body, Order.class);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                return auth.get_http_request(String.format(FORMAT_USER_ORDER, id), "GET");
            }
        }.request();
    }

    public static List<IShop> shops() throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<List<IShop>>(){

            @Override
            public List<IShop> call(RequestResult rr) {
                System.out.println("body:" + rr.body);
                Type collectionType = new TypeToken<List<Shop>>(){}.getType();
                Gson gson = new Gson();
                return gson.fromJson(rr.body, collectionType);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                return auth.get_http_request(SHOPS, "GET");
            }
        }.request();
    }

    public static List<IGood> shop_goods(final String shop_id) throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<List<IGood>>(){

            @Override
            public List<IGood> call(RequestResult rr) {
                System.out.println("body:" + rr.body);
                Type collectionType = new TypeToken<List<Good>>(){}.getType();
                Gson gson = new Gson();
                return gson.fromJson(rr.body, collectionType);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                return auth.get_http_request(String.format(FORMAT_SHOP_GOODS, shop_id), "GET");
            }
        }.request();
    }

    public static IShopCart get_cart(final String shop_id) throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        System.out.println("shop id:" + shop_id);
        return new RequestProcess<IShopCart>(){

            @Override
            public IShopCart call(RequestResult rr) {
                System.out.println("body:" + rr.body);
                Gson gson = new Gson();
                return gson.fromJson(rr.body, ShopCart.class);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                return auth.get_http_request(String.format(FORMAT_SHOP_CART, shop_id), "GET");
            }
        }.request();
    }
    //////////////////

    /**
     * http api method end
     ***********************************************************************************/


    public abstract static class RequestProcess<T>{
        public T request() throws AuthErrorException, RequestDataErrorException, NetworkErrorException{
            AuthenticatorsController auth = new AuthenticatorsController(PaopaoCustomerApplication.get_context());
            HttpRequest request = build_request(auth);
            RequestResult rr = auth.syn_request(request);
            if(rr == null) throw new NetworkErrorException();
            System.out.println("rr.status :" + rr.status );
            if(rr.status == 200){
                return call(rr);
            }else if(rr.status == 401){
                throw new AuthErrorException();
            }else{
                throw new RequestDataErrorException();
            }
        };

        public abstract T call(RequestResult rr);

        public abstract HttpRequest build_request(AuthenticatorsController auth);
    }

    public static class AuthErrorException extends Exception{};
    public static class RequestDataErrorException extends Exception{};
    public static class NetworkErrorException extends Exception{};
}
