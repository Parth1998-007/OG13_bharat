package com.organicbharat.api_model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {
    public static class LoginReq extends BaseReq {
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("login_type")
        @Expose
        private int login_type;
        @SerializedName("device_type")
        @Expose
        private String device_type = "android";
        @SerializedName("token")
        @Expose
        private String token;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getLogin_type() {
            return login_type;
        }

        public void setLogin_type(int login_type) {
            this.login_type = login_type;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);

        }

    }

    public static class LoginRes extends BaseRes {
        @SerializedName("items_in_cart")
        @Expose
        private int items_in_cart;
        @SerializedName("Profile")
        @Expose
        private ProfileData Profile;

        public int getItems_in_cart() {
            return items_in_cart;
        }

        public void setItems_in_cart(int items_in_cart) {
            this.items_in_cart = items_in_cart;
        }

        public ProfileData getProfile() {
            return Profile;
        }

        public void setProfile(ProfileData profile) {
            Profile = profile;
        }
    }

    public static class ProfileData {
        @SerializedName("pk_user_id")
        @Expose
        private int userId;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("api_key")
        @Expose
        private String api_key;
        @SerializedName("device_type")
        @Expose
        private String device_type;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("is_verified")
        @Expose
        private int is_verified;
        @SerializedName("wallet")
        @Expose
        private int wallet;
        @SerializedName("created_date")
        @Expose
        private String created_date;
        @SerializedName("updated_date")
        @Expose
        private String updated_date;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getApi_key() {
            return api_key;
        }

        public void setApi_key(String api_key) {
            this.api_key = api_key;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getIs_verified() {
            return is_verified;
        }

        public void setIs_verified(int is_verified) {
            this.is_verified = is_verified;
        }

        public int getWallet() {
            return wallet;
        }

        public void setWallet(int wallet) {
            this.wallet = wallet;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }
    }
}
