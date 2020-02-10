package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClearCartModel {
    public static class ClearCartReq extends BaseReq{
        @SerializedName("user_id")
        @Expose
        private int user_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }

    public static class ClearCartRes extends BaseRes{

    }
}
