package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PauseOrderModel {
    public static class PauseOrderReq extends BaseReq{
        @SerializedName("user_id")
        @Expose
        private int user_id;
        @SerializedName("start_date")
        @Expose
        private String start_date;
        @SerializedName("end_date")
        @Expose
        private String end_date;
        @SerializedName("order_id")
        @Expose
        private int order_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }
    }

    public static class PauseOrderRes extends BaseRes{

    }
}
