package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MySubscriptionOrderModel {

    public static class MySubscriptionOrderRes extends BaseRes{
        @SerializedName("order_list")
        @Expose
        private ArrayList<OrderList> orderlist;

        public ArrayList<OrderList> getOrderlist() {
            return orderlist;
        }

        public void setOrderlist(ArrayList<OrderList> orderlist) {
            this.orderlist = orderlist;
        }
    }

    public static class OrderList{
        @SerializedName("order_id")
        @Expose
        private int order_id;
        @SerializedName("unique_order_id")
        @Expose
        private String unique_order_id;
        @SerializedName("is_subscription")
        @Expose
        private int is_subscription;
        @SerializedName("start_date")
        @Expose
        private String start_date;
        @SerializedName("end_date")
        @Expose
        private String end_date;
        @SerializedName("promocode")
        @Expose
        private String promocode;
        @SerializedName("gst")
        @Expose
        private float gst;
        @SerializedName("delivery_charge")
        @Expose
        private float delivery_charge;
        @SerializedName("used_wallet_amount")
        @Expose
        private String used_wallet_amount;
        @SerializedName("discount")
        @Expose
        private int discount;
        @SerializedName("delilvery_date")
        @Expose
        private String delilvery_date;
        @SerializedName("created_date")
        @Expose
        private String created_date;
        @SerializedName("delivery_address")
        @Expose
        private String delivery_address;
        @SerializedName("payable_amount")
        @Expose
        private String payable_amount;

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getUnique_order_id() {
            return unique_order_id;
        }

        public void setUnique_order_id(String unique_order_id) {
            this.unique_order_id = unique_order_id;
        }

        public int getIs_subscription() {
            return is_subscription;
        }

        public void setIs_subscription(int is_subscription) {
            this.is_subscription = is_subscription;
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

        public String getPromocode() {
            return promocode;
        }

        public void setPromocode(String promocode) {
            this.promocode = promocode;
        }


        public float getGst() {
            return gst;
        }

        public void setGst(float gst) {
            this.gst = gst;
        }

        public float getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(float delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public String getUsed_wallet_amount() {
            return used_wallet_amount;
        }

        public void setUsed_wallet_amount(String used_wallet_amount) {
            this.used_wallet_amount = used_wallet_amount;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public String getDelilvery_date() {
            return delilvery_date;
        }

        public void setDelilvery_date(String delilvery_date) {
            this.delilvery_date = delilvery_date;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getDelivery_address() {
            return delivery_address;
        }

        public void setDelivery_address(String delivery_address) {
            this.delivery_address = delivery_address;
        }

        public String getPayable_amount() {
            return payable_amount;
        }

        public void setPayable_amount(String payable_amount) {
            this.payable_amount = payable_amount;
        }
    }
}
