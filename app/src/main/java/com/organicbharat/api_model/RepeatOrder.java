package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RepeatOrder {

    public static class RepeatOrderReq extends BaseReq{
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

    public static class RepeatOrderRes extends BaseRes{
        @SerializedName("data")
        @Expose
        private ArrayList<MyCartModel.Data> datalist;
        @SerializedName("payable")
        @Expose
        private int payable;
        @SerializedName("gst")
        @Expose
        private double gst;
        @SerializedName("delivery_charge")
        @Expose
        private double delivery_charge;
        @SerializedName("error_item")
        @Expose
        private ArrayList<ErrorItem> errorItemList;
        @SerializedName("items_in_cart")
        @Expose
        private int items_in_cart;

        public ArrayList<MyCartModel.Data> getDatalist() {
            return datalist;
        }

        public void setDatalist(ArrayList<MyCartModel.Data> datalist) {
            this.datalist = datalist;
        }

        public int getPayable() {
            return payable;
        }

        public void setPayable(int payable) {
            this.payable = payable;
        }

        public double getGst() {
            return gst;
        }

        public void setGst(double gst) {
            this.gst = gst;
        }

        public double getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(double delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public ArrayList<ErrorItem> getErrorItemList() {
            return errorItemList;
        }

        public void setErrorItemList(ArrayList<ErrorItem> errorItemList) {
            this.errorItemList = errorItemList;
        }

        public int getItems_in_cart() {
            return items_in_cart;
        }

        public void setItems_in_cart(int items_in_cart) {
            this.items_in_cart = items_in_cart;
        }
    }

    public static class Data{
        @SerializedName("cart_id")
        @Expose
        private int cart_id;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("product_id")
        @Expose
        private int product_id;
        @SerializedName("product")
        @Expose
        private String product;
        @SerializedName("product_img")
        @Expose
        private String product_img;
        @SerializedName("qty")
        @Expose
        private int qty;
        @SerializedName("rate")
        @Expose
        private int rate;
        @SerializedName("remaining_qty")
        @Expose
        private int remaining_qty;

        public int getCart_id() {
            return cart_id;
        }

        public void setCart_id(int cart_id) {
            this.cart_id = cart_id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getProduct_img() {
            return product_img;
        }

        public void setProduct_img(String product_img) {
            this.product_img = product_img;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

        public int getRemaining_qty() {
            return remaining_qty;
        }

        public void setRemaining_qty(int remaining_qty) {
            this.remaining_qty = remaining_qty;
        }
    }

    public static class ErrorItem {
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("product")
        @Expose
        private String product;
        @SerializedName("product_image")
        @Expose
        private String product_image;
        @SerializedName("product_price")
        @Expose
        private String product_price;
        @SerializedName("category_name")
        @Expose
        private String category_name;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
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

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }
    }
}
