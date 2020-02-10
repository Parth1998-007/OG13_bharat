package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResumeOrder {
    public static class ResumeOrderReq extends BaseReq{
        @SerializedName("order_id")
        @Expose
        private int order_id;
        @SerializedName("user_id")
        @Expose
        private int user_id;

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }

    public static class ResumeOrderRes extends BaseRes{

    }
}
