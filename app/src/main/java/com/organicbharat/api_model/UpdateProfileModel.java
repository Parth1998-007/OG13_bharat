package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileModel
{
    public static class UpdateProfileReq extends BaseReq {

        @SerializedName("email")
        @Expose
        private String email;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @SerializedName("username")
        @Expose
        private String username;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        @SerializedName("user_id")
        @Expose
        private int userId;

        @SerializedName("phone")
        @Expose
        private String mobile;


        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }




        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }



    }

    public static class UpdateProfileRes extends BaseRes {
        @SerializedName("user_id")
        @Expose
        private String user_id;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        @SerializedName("username")
        @Expose
        private String user_name;
        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("phone")
        @Expose
        private String mobile;

        @SerializedName("api_key")
        @Expose
        private String api_key;


        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }



        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }



        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getApi_key() {
            return api_key;
        }

        public void setApi_key(String api_key) {
            this.api_key = api_key;
        }
    }
}
