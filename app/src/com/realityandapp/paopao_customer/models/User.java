package com.realityandapp.paopao_customer.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.mindpin.android.authenticator.IUser;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.http.Uploader;
import com.realityandapp.paopao_customer.models.interfaces.ICartGoodsData;
import com.realityandapp.paopao_customer.networks.HttpApi;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Calendar;

/**
 * Created by dd on 14-6-10.
 */
@Table(name = "Users")
public class User extends IUser implements Serializable {
    private static final String TAG = "User";

    // 下面几个字段根据具体业务逻辑确定
    @SerializedName("_id")
    @Column(name = "Server_user_id")
    public String server_user_id;

    @Column(name = "Name")
    public String name;
    @Column(name = "Email")
    public String email;
    @Column(name = "Login")
    public String login;
    @Column(name = "Phone")
    public String phone;
    //    @Column(name = "Avatar")
    public Uploader avatar;
    @Column(name = "IM_UUID")
    public String im_uuid;

    @Column(name = "signed_in_at", index = true)
    public long signed_in_at;

    public User() {
        signed_in_at = Calendar.getInstance().getTimeInMillis();
    }

    public static User find(String server_user_id) {
        return new Select()
                .from(User.class)
                .where("Server_user_id = ?", server_user_id)
                .executeSingle();
    }

    public static User current() {
        Select select = new Select();
        if (select != null &&
                select
                        .from(User.class)
                        .orderBy("signed_in_at DESC").count() > 0)
            return new Select()
                    .from(User.class)
                    .orderBy("signed_in_at DESC")
                    .executeSingle();
        else
            return null;
    }

    @Override
    public boolean equals(Object o) {
        try {
            User tmp = (User) o;
            return tmp.server_user_id.equals(server_user_id);
        } catch (Exception ex) {
            return super.equals(o);
        }
    }

    public static void delete_all() {
        new Delete().from(User.class).execute();
    }

    public String get_avatar_url() {
        return HttpApi.SITE + avatar.get_default();
    }
}
