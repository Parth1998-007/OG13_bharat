package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyCartModel{

    public static class MyCartModelRes extends BaseRes {
        @SerializedName("data")
        @Expose
        private ArrayList<Data> datlist;
        @SerializedName("payable")
        @Expose
        private float payable;
        @SerializedName("gst")
        @Expose
        private float gst;
        @SerializedName("delivery_charge")
        @Expose
        private float delivery_charge;
        @SerializedName("items_in_cart")
        @Expose
        private int items_incart;

        @SerializedName("availablity")
        @Expose
        private int availablity;

        public int getAvailablity() {
            return availablity;
        }

        public void setAvailablity(int availablity) {
            this.availablity = availablity;
        }

        public ArrayList<Data> getDatlist() {
            return datlist;
        }

        public void setDatlist(ArrayList<Data> datlist) {
            this.datlist = datlist;
        }

        public float getPayable() {
            return payable;
        }

        public void setPayable(float payable) {
            this.payable = payable;
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

        public int getItems_incart() {
            return items_incart;
        }

        public void setItems_incart(int items_incart) {
            this.items_incart = items_incart;
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
        @SerializedName("product_img")
        @Expose
        private String prod_image;
        @SerializedName("qty")
        @Expose
        private int qty;
        @SerializedName("rate")
        @Expose
        private String rate;
        @SerializedName("discounted_rate")
        @Expose
        private String discounted_rate;
        @SerializedName("remaining_qty")
        @Expose
        private int remaining_qty;


        @SerializedName("isavailable")
        @Expose
        private boolean isavailable;

        public String getDiscounted_rate() {
            return discounted_rate;
        }

        public void setDiscounted_rate(String discounted_rate) {
            this.discounted_rate = discounted_rate;
        }

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

        public boolean isavailable() {
            return isavailable;
        }

        public void isavailable(boolean isavailable) {
            this.isavailable = isavailable;
        }
    }


}
