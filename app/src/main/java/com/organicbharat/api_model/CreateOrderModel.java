package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateOrderModel {
    public static class CreateOrderReq extends BaseReq{
        @SerializedName("user_id")
        @Expose
        private int user_id;
        @SerializedName("address_id")
        @Expose
        private int address_id;
        @SerializedName("total")
        @Expose
        private String total;
        @SerializedName("delivery_charge")
        @Expose
        private String delivery_charge;
        @SerializedName("gst")
        @Expose
        private String gst;
        @SerializedName("discount")
        @Expose
        private String discount;
        @SerializedName("promocode")
        @Expose
        private String promocode;
        @SerializedName("payable_amount")
        @Expose
        private String payable_amount;
        @SerializedName("time_slot")
        @Expose
        private String time_slot;

        public String getTxnId()
        {
            return txnId;
        }

        public void setTxnId(String txnId) {
            this.txnId = txnId;
        }

        @SerializedName("txn_id")
        @Expose
        private String txnId;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getAddress_id() {
            return address_id;
        }

        public void setAddress_id(int address_id) {
            this.address_id = address_id;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public String getGst() {
            return gst;
        }

        public void setGst(String gst) {
            this.gst = gst;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPromocode() {
            return promocode;
        }

        public void setPromocode(String promocode) {
            this.promocode = promocode;
        }

        public String getPayable_amount() {
            return payable_amount;
        }

        public void setPayable_amount(String payable_amount) {
            this.payable_amount = payable_amount;
        }

        public String getTime_slot() {
            return time_slot;
        }

        public void setTime_slot(String time_slot) {
            this.time_slot = time_slot;
        }
    }

    public static class CreateOrderRes extends BaseRes{
        @SerializedName("order_id")
        @Expose
        private int order_id;

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }
    }
}
