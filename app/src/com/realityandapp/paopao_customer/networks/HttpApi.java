package com.realityandapp.paopao_customer.networks;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mindpin.android.authenticator.RequestResult;
import com.realityandapp.paopao_customer.PaopaoCustomerApplication;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.controllers.AuthenticatorsController;
import com.realityandapp.paopao_customer.models.interfaces.*;
import com.realityandapp.paopao_customer.models.http.Order;
import com.realityandapp.paopao_customer.models.http.Shop;
import com.realityandapp.paopao_customer.models.http.Good;
import com.realityandapp.paopao_customer.models.http.ShopCart;
import com.realityandapp.paopao_customer.models.test.Address;

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
    public static final String FORMAT_SHOP_CART = SITE + "/shops/%s/cart.json";
    public static final String FORMAT_GOOD = SITE + "/goods/%s.json";
    public static final String FORMAT_SHOP = SITE + "/shops/%s.json";
    public static final String FORMAT_SUBMIT_CART = SITE + "/shops/%s/submit_cart.json";
    public static final String USER_ADDRESSES = USER_SITE + "/addresses.json";

    /**
     * http api url end
     */


    public static List<IOrder> user_orders() throws AuthErrorException, RequestDataErrorException, NetworkErrorException {
        return new RequestProcess<List<IOrder>>(){

            @Override
            public List<IOrder> call(RequestResult rr) {
                System.out.println("orders body:" + rr.body);
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
                System.out.println(SHOPS);
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

    public static IGood good(final String good_id) throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        System.out.println("good id:" + good_id);
        return new RequestProcess<IGood>(){

            @Override
            public IGood call(RequestResult rr) {
                System.out.println("good body:" + rr.body);
                Gson gson = new Gson();
                return gson.fromJson(rr.body, Good.class);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                return auth.get_http_request(String.format(FORMAT_GOOD, good_id), "GET");
            }
        }.request();
    }

    public static IShop shop(final String shop_id) throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        System.out.println("shop id:" + shop_id);
        return new RequestProcess<IShop>(){

            @Override
            public IShop call(RequestResult rr) {
                System.out.println("good body:" + rr.body);
                Gson gson = new Gson();
                return gson.fromJson(rr.body, Shop.class);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                return auth.get_http_request(String.format(FORMAT_SHOP, shop_id), "GET");
            }
        }.request();
    }

    public static IOrder shop_cart_to_order(final IShopCart shop_cart) throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<IOrder>(){

            @Override
            public IOrder call(RequestResult rr) {
                System.out.println("order body:" + rr.body);
                Gson gson = new Gson();
                return gson.fromJson(rr.body, Order.class);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                System.out.println("String.format(FORMAT_SUBMIT_CART, shop_cart.get_shop_id()):"
                    + String.format(FORMAT_SUBMIT_CART, shop_cart.get_shop_id()));
                HttpRequest request =
                        auth.get_http_request(String.format(FORMAT_SUBMIT_CART, shop_cart.get_shop_id()), "POST");
                request.accept("application/json");
                // todo for not with expose
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(ShopCart.class, new ShopCart.ShopCartSerializer())
                        .create();
                String json = gson.toJson(shop_cart);

//                Gson gson_without = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//                String json = gson_without.toJson(shop_cart, ShopCart.class);
//                String json = new Gson().toJson(shop_cart, ShopCart.class);
                System.out.println("json:\r\n" + json);
                request.send(json);
                return request;
            }
        }.request();
    }

    public static List<IAddress> my_addresses() throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<List<IAddress>>(){

            @Override
            public List<IAddress> call(RequestResult rr) {
                System.out.println("addresses body:" + rr.body);
                Type collectionType = new TypeToken<List<Address>>(){}.getType();
                Gson gson = new Gson();
                return gson.fromJson(rr.body, collectionType);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                HttpRequest request = auth.get_http_request(USER_ADDRESSES, "GET");
                request.accept("application/json");
                return request;
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
