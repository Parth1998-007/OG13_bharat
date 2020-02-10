package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class MyOrderModel {

    public static class MyOrderListRes extends BaseRes{

        @SerializedName("order")
        @Expose
        private ArrayList<Order> orderlist;

        public ArrayList<Order> getOrderlist() {
            return orderlist;
        }

        public void setOrderlist(ArrayList<Order> orderlist) {
            this.orderlist = orderlist;
        }

    }

    public static class Order{

        @SerializedName("order_id")
        @Expose
        private int order_id;
        @SerializedName("unique_order_id")
        @Expose
        private String unique_order_id;
        @SerializedName("is_subscription")
        @Expose
        private int is_subscription;
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
        @SerializedName("order_status")
        @Expose
        private String order_status;
        @SerializedName("rider_status")
        @Expose
        private int rider_status;

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

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public int getRider_status() {
            return rider_status;
        }

        public void setRider_status(int rider_status) {
            this.rider_status = rider_status;
        }
    }

    public static class OrderDetailRes extends BaseRes{
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
        @SerializedName("completed_orders")
        @Expose
        private int completed_orders;
        @SerializedName("left_orders")
        @Expose
        private int left_orders;
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
        @SerializedName("delivery_date")
        @Expose
        private String delivery_date;

        @SerializedName("delivery_time")
        @Expose
        private String delivery_time;

        @SerializedName("created_date")
        @Expose
        private String created_date;
        @SerializedName("order_items")
        @Expose
        private ArrayList<OrderItem> orderitemlist;
        @SerializedName("delivery_address")
        @Expose
        private String delivery_address;
        @SerializedName("total_amount")
        @Expose
        private int total_amount;
        @SerializedName("order_status")
        @Expose
        private String order_status;


        public ArrayList<Summary> getSummaryList() {
            return summaryList;
        }

        public void setSummaryList(ArrayList<Summary> summaryList) {
            this.summaryList = summaryList;
        }

        @SerializedName("summary")
        @Expose
        private ArrayList<Summary> summaryList;


        @SerializedName("rider_status")
        @Expose
        private int rider_status;
        @SerializedName("payable_amount")
        @Expose
        private String payable_amount;

        @SerializedName("pdf")
        @Expose
        private String pdf;

        public String getDelivery_time() {
            return delivery_time;
        }

        public void setDelivery_time(String delivery_time) {
            this.delivery_time = delivery_time;
        }

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

        public String getDelivery_date() {
            return delivery_date;
        }

        public void setDelivery_date(String delivery_date) {
            this.delivery_date = delivery_date;
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

        public int getCompleted_orders() {
            return completed_orders;
        }

        public void setCompleted_orders(int completed_orders) {
            this.completed_orders = completed_orders;
        }

        public int getLeft_orders() {
            return left_orders;
        }

        public void setLeft_orders(int left_orders) {
            this.left_orders = left_orders;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public ArrayList<OrderItem> getOrderitemlist() {
            return orderitemlist;
        }

        public void setOrderitemlist(ArrayList<OrderItem> orderitemlist) {
            this.orderitemlist = orderitemlist;
        }

        public String getDelivery_address() {
            return delivery_address;
        }

        public void setDelivery_address(String delivery_address) {
            this.delivery_address = delivery_address;
        }

        public int getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(int total_amount) {
            this.total_amount = total_amount;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public int getRider_status() {
            return rider_status;
        }

        public void setRider_status(int rider_status) {
            this.rider_status = rider_status;
        }

        public String getPayable_amount() {
            return payable_amount;
        }

        public void setPayable_amount(String payable_amount) {
            this.payable_amount = payable_amount;
        }

        public String getPdf() {
            return pdf;
        }

        public void setPdf(String pdf) {
            this.pdf = pdf;
        }
    }

    public static class OrderItem {
        @SerializedName("category_name")
        @Expose
        private String category_name;
        @SerializedName("product_id")
        @Expose
        private int product_id;
        @SerializedName("product_name")
        @Expose
        private String product_name;
        @SerializedName("product_image")
        @Expose
        private String product_image;
        @SerializedName("product_price")
        @Expose
        private String product_price;
        @SerializedName("quantity")
        @Expose
        private int quantity;
        @SerializedName("order_date")
        @Expose
        private String order_date;
        @SerializedName("order_status")
        @Expose
        private int order_status;
        @SerializedName("rider_status")
        @Expose
        private int rider_status;

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_image() {
            return product_image;
        }

        public void setProduct_image(String product_image) {
            this.product_image = product_image;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getOrder_date() {
            return order_date;
        }

        public void setOrder_date(String order_date) {
            this.order_date = order_date;
        }

        public int getOrder_status() {
            return order_status;
        }

        public void setOrder_status(int order_status) {
            this.order_status = order_status;
        }

        public int getRider_status() {
            return rider_status;
        }

        public void setRider_status(int rider_status) {
            this.rider_status = rider_status;
        }
    }

    public static class Summary
    {
        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        @SerializedName("order_type")
        @Expose
        private String orderType;
    }

}
