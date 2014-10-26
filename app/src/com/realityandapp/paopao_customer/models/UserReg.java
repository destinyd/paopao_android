package com.realityandapp.paopao_customer.models;

/**
 * Created by dd on 14-10-26.
 */
public class UserReg {
    UserTmp user;

    public UserReg(String phone, String verify_code, String password, String name, String email) {
        user = new UserTmp(phone, verify_code, password, name, email);
    }

    public class UserTmp{
        public String email, password, password_confirmation, phone, verify_code, name;

        public UserTmp(String phone, String verify_code, String password, String name, String email) {
            this.phone = phone;
            this.verify_code = verify_code;
            this.password = password;
            this.name = name;
            this.email = email;
        }
    }
}
