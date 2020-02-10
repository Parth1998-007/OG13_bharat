package com.organicbharat.api_model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CartModel {

    public static class AddtoCartReq extends BaseReq{
        @SerializedName("user_id")
        @Expose
        private int user_id;
        @SerializedName("product_id")
        @Expose
        private int product_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);

        }
    }

    public static class AddtoCartRes extends BaseRes{

        @SerializedName("data")
        @Expose
        private ArrayList<Data> datlist;
        @SerializedName("payable")
        @Expose
        private int payable;
        @SerializedName("items_in_cart")
        @Expose
        private int items_in_cart;

        public ArrayList<Data> getDatlist() {
            return datlist;
        }

        public void setDatlist(ArrayList<Data> datlist) {
            this.datlist = datlist;
        }

        public int getPayable() {
            return payable;
        }

        public void setPayable(int payable) {
            this.payable = payable;
        }

        public int getItems_in_cart() {
            return items_in_cart;
        }

        public void setItems_in_cart(int items_in_cart) {
            this.items_in_cart = items_in_cart;
        }
    }

    public static class Data {
        @SerializedName("cart_id")
        @Expose
        private int cartid;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("product_id")
        @Expose
        private int product_id;
        @SerializedName("product")
        @Expose
        private String product;
        @SerializedName("prod_image")
        @Expose
        private String prod_image;
        @SerializedName("qty")
        @Expose
        private int qty;
        @SerializedName("rate")
        @Expose
        private String rate;
        @SerializedName("remaining_qty")
        @Expose
        private int remaining_qty;

        public int getCartid() {
            return cartid;
        }

        public void setCartid(int cartid) {
            this.cartid = cartid;
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

        public String getProd_image() {
            return prod_image;
        }

        public void setProd_image(String prod_image) {
            this.prod_image = prod_image;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public int getRemaining_qty() {
            return remaining_qty;
        }

        public void setRemaining_qty(int remaining_qty) {
            this.remaining_qty = remaining_qty;
        }
    }

    public static class RemoveFromCartReq extends BaseReq{
        @SerializedName("user_id")
        @Expose
        private int user_id;
        @SerializedName("product_id")
        @Expose
        private int product_id;
        @SerializedName("qty")
        @Expose
        private int qty;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }
    }

    public static class RemoveFromCartRes extends BaseRes{
        @SerializedName("data")
        @Expose
        private ArrayList<Data> datlist;
        @SerializedName("payable")
        @Expose
        private int payable;
        @SerializedName("items_in_cart")
        @Expose
        private int items_in_cart;

        public ArrayList<Data> getDatlist() {
            return datlist;
        }

        public void setDatlist(ArrayList<Data> datlist) {
            this.datlist = datlist;
        }

        public int getPayable() {
            return payable;
        }

        public void setPayable(int payable) {
            this.payable = payable;
        }

        public int getItems_in_cart() {
            return items_in_cart;
        }

        public void setItems_in_cart(int items_in_cart) {
            this.items_in_cart = items_in_cart;
        }
    }
}
