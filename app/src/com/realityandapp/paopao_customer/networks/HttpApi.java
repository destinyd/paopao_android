package com.realityandapp.paopao_customer.networks;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mindpin.android.authenticator.RequestResult;
import com.realityandapp.paopao_customer.PaopaoCustomerApplication;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.controllers.AuthenticatorsController;
import com.realityandapp.paopao_customer.models.User;
import com.realityandapp.paopao_customer.models.interfaces.*;
import com.realityandapp.paopao_customer.models.http.Order;
import com.realityandapp.paopao_customer.models.http.Shop;
import com.realityandapp.paopao_customer.models.http.Good;
import com.realityandapp.paopao_customer.models.http.ShopCart;
import com.realityandapp.paopao_customer.models.http.Address;

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
    public static final String DEFAULT_ADDRESS = USER_SITE + "/addresses/default.json";
    public static final String FORMAT_SET_DEFAULT_ADDRESS = USER_SITE + "/addresses/%s/set_default.json";
    public static final String FORMAT_CALCULATE_DISTANCE = USER_SITE + "/addresses/%s/calculate_distance.json";
    public static final String USERS = SITE + "/users.json";
    public static final String GET_VERIFY_CODE = SITE + "/users/get_reg_verify_code.json";
    public static final String USER_INFO = USER_SITE + "/userinfo.json";
    public static final String FORMAT_IM_ACCOUNTS = SITE + "/im/%s.json";

    /**
     * http api url end
     */


    public static List<IOrder> user_orders() throws AuthErrorException, RequestDataErrorException, NetworkErrorException {
        return new RequestProcess<List<IOrder>>() {

            @Override
            public List<IOrder> call(RequestResult rr) {
                System.out.println("orders body:" + rr.body);
                Type collectionType = new TypeToken<List<Order>>() {
                }.getType();
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
        return new RequestProcess<IOrder>() {

            @Override
            public IOrder call(RequestResult rr) {
                System.out.println("my order body:" + rr.body);
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
        return new RequestProcess<List<IShop>>() {

            @Override
            public List<IShop> call(RequestResult rr) {
                System.out.println("shops body:" + rr.body);
                Type collectionType = new TypeToken<List<Shop>>() {
                }.getType();
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
        return new RequestProcess<List<IGood>>() {

            @Override
            public List<IGood> call(RequestResult rr) {
                System.out.println("body:" + rr.body);
                Type collectionType = new TypeToken<List<Good>>() {
                }.getType();
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
        return new RequestProcess<IShopCart>() {

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
        return new RequestProcess<IGood>() {

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
        return new RequestProcess<IShop>() {

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
        return new RequestProcess<IOrder>() {

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
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(ShopCart.class, new ShopCart.ShopCartSerializer())
                                .create();
                String json = gson.toJson(shop_cart);
                System.out.println("shop_cart_to_order json:\r\n" + json);
//                request.send(json);
                request.part("cart", json);
                return request;
            }
        }.request();
    }

    public static List<IAddress> my_addresses() throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<List<IAddress>>() {

            @Override
            public List<IAddress> call(RequestResult rr) {
                System.out.println("addresses body:" + rr.body);
                Type collectionType = new TypeToken<List<Address>>() {
                }.getType();
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

    public static IAddress default_address() throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<IAddress>() {

            @Override
            public IAddress call(RequestResult rr) {
                System.out.println("default address body:" + rr.body);
                Gson gson = new Gson();
                return gson.fromJson(rr.body, Address.class);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                return auth.get_http_request(DEFAULT_ADDRESS, "GET");
            }
        }.request();
    }

    public static IAddress set_default_address(final String address_id) throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<IAddress>() {

            @Override
            public IAddress call(RequestResult rr) {
                System.out.println("address_id body:" + rr.body);
                Gson gson = new Gson();
                return gson.fromJson(rr.body, Address.class);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                HttpRequest request = auth.get_http_request(String.format(FORMAT_SET_DEFAULT_ADDRESS, address_id), "POST");
                request.accept("application/json");
                request.send("");
                return request;
            }
        }.request();
    }

    public static JsonObject calculate_distance_and_pricing(final String shop_id, final String address_id) throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<JsonObject>() {

            @Override
            public JsonObject call(RequestResult rr) {
                System.out.println("calculate_distance_and_pricing body:" + rr.body);
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(rr.body, JsonObject.class);
                return jsonObject;
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                HttpRequest request = auth.get_http_request(String.format(FORMAT_CALCULATE_DISTANCE, address_id), "POST");
                request.accept("application/json");
                request.part("shop_id", shop_id);
                return request;
            }
        }.request();
    }

    public static IShopCart save_shop_cart(final IShopCart shop_cart) throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<IShopCart>() {

            @Override
            public IShopCart call(RequestResult rr) {
                System.out.println("order body:" + rr.body);
                Gson gson = new Gson();
                return gson.fromJson(rr.body, ShopCart.class);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                HttpRequest request =
                        auth.get_http_request(String.format(FORMAT_SHOP_CART, shop_cart.get_shop_id()), "POST");
                request.accept("application/json");
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(ShopCart.class, new ShopCart.ShopCartSerializer())
                                .create();
                String json = gson.toJson(shop_cart);
                System.out.println("shop cart json:\r\n" + json);
                request.part("cart", json);
//                request.send(json);
                return request;
            }
        }.request();
    }

    public static Boolean destroy_order(final String order_id) throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<Boolean>() {

            @Override
            public Boolean call(RequestResult rr) {
                System.out.println("destroy_order body:" + rr.body);
                return rr.is_ok();
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                HttpRequest request = auth.get_http_request(String.format(FORMAT_USER_ORDER, order_id), "DELETE");
                request.accept("application/json");
                return request;
            }
        }.request();
    }

    public static Boolean save_order(final IOrder order) throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<Boolean>() {

            @Override
            public Boolean call(RequestResult rr) {
                System.out.println("destroy_order body:" + rr.body);
                return rr.is_ok();
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                HttpRequest request = auth.get_http_request(String.format(FORMAT_USER_ORDER, order.get_id()), "PUT");
                request.accept("application/json");

                Gson gson =
                        new GsonBuilder().registerTypeAdapter(Order.class, new Order.OrderSerializer())
                                .create();
                String json = gson.toJson(order);
                System.out.println("save order json:\r\n" + json);
//                request.send(json);
                request.part("order", json);
                return request;
            }
        }.request();
    }

    public static IAddress save_address(final IAddress address) throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<IAddress>() {

            @Override
            public IAddress call(RequestResult rr) {
                System.out.println("save_address body:" + rr.body);
                Gson gson = new Gson();
                return gson.fromJson(rr.body, Address.class);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                HttpRequest request = auth.get_http_request(USER_ADDRESSES, "POST");
                request.accept("application/json");

//                Gson gson =
//                        new GsonBuilder().registerTypeAdapter(Address.class, new Address.AddressSerializer())
//                                .create();
//                String json = gson.toJson(order);
                String json = new Gson().toJson(address, Address.class);
                System.out.println("order json:\r\n" + json);
                request.part("address", json);
                return request;
            }
        }.request();
    }

    public static User user_info() throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<User>() {

            @Override
            public User call(RequestResult rr) {
                System.out.println("save_address body:" + rr.body);
                Gson gson = new Gson();
                return gson.fromJson(rr.body, User.class);
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                HttpRequest request = auth.get_http_request(USER_INFO, "GET");
                request.accept("application/json");
                return request;
            }
        }.request();
    }

    public static String sign_up(
            final String phone, final String verify_code, final String password,
            final String name, final String email
    ) {
        HttpRequest request = HttpRequest.post(USERS);
        request.accept("application/json");
        request.part("user[phone]", phone);
        request.part("user[verify_code]", verify_code);
        request.part("user[password]", password);
        request.part("user[name]", name);
        request.part("user[email]", email);
//        UserReg tmp = new UserReg(phone, verify_code, password, name, email);
//        String json = new Gson().toJson(tmp, UserReg.class);
//        System.out.println("sign up json:" + json);
//        request.send(json);
        String body = request.body();
        int code = request.code();
        System.out.println("sign up body:" + body);
        System.out.println("sign up code:" + code);
        return body;
//        if (code >= 200 && code < 300) {
//            Gson gson = new Gson();
//            return gson.fromJson(body, User.class);
//        } else {
//            return null;
//        }
    }

    public static Integer get_verify_code(String phone) {
        HttpRequest request = HttpRequest.post(GET_VERIFY_CODE);
        request.part("phone", phone);
        String result = request.body();
        System.out.println("get_verify_code body:" + result);
        JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
        Integer time_left = -1;
        try {
            time_left = jsonObject.get("retry_time_left").getAsInt();
        } catch (Exception ex) {
        }
        return time_left;
    }

    public static String im_nickname(final String im_id)  throws RequestDataErrorException, AuthErrorException, NetworkErrorException {
        return new RequestProcess<String>() {

            @Override
            public String call(RequestResult rr) {
                System.out.println("im_accounts body:" + rr.body);
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(rr.body, JsonObject.class);
                return jsonObject.get("im_nickname").getAsString();
            }

            @Override
            public HttpRequest build_request(AuthenticatorsController auth) {
                HttpRequest request = auth.get_http_request(String.format(FORMAT_IM_ACCOUNTS, im_id), "GET");
                return request;
            }
        }.request();
    }

    //////////////////

    /**
     * http api method end
     * *********************************************************************************
     */


    public abstract static class RequestProcess<T> {
        public T request() throws AuthErrorException, RequestDataErrorException, NetworkErrorException {
            AuthenticatorsController auth = new AuthenticatorsController(PaopaoCustomerApplication.get_context());
            HttpRequest request = build_request(auth);
            RequestResult rr = auth.syn_request(request);
            if (rr == null) throw new NetworkErrorException();
            System.out.println("rr.status :" + rr.status);
            if (rr.status >= 200 && rr.status < 300) {
                return call(rr);
            } else if (rr.status == 401) {
                throw new AuthErrorException();
            } else {
                throw new RequestDataErrorException();
            }
        }

        public abstract T call(RequestResult rr);

        public abstract HttpRequest build_request(AuthenticatorsController auth);
    }

    public static class AuthErrorException extends Exception {
    }

    public static class RequestDataErrorException extends Exception {
    }

    public static class NetworkErrorException extends Exception {
    }
}
