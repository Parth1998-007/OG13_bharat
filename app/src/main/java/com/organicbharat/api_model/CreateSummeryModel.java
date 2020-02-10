package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CreateSummeryModel {

    public static class CreateSummeryReq extends BaseReq{
        @SerializedName("address_id")
        @Expose
        private int address_id;
        @SerializedName("requirement")
        @Expose
        private String requirement;
        @SerializedName("odd_day")
        @Expose
        private int odd_day;
        @SerializedName("even_day")
        @Expose
        private int even_day;
        @SerializedName("user_id")
        @Expose
        private int user_id;
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

        @SerializedName("start_date")
        @Expose
        private String start_date;
        @SerializedName("end_date")
        @Expose
        private String end_date;
        @SerializedName("items")
        @Expose
        private ArrayList<Item> itemlist;
        @SerializedName("days")
        @Expose
        private Days days;

        public int getAddress_id() {
            return address_id;
        }

        public void setAddress_id(int address_id) {
            this.address_id = address_id;
        }

        public String getRequirement() {
            return requirement;
        }

        public void setRequirement(String requirement) {
            this.requirement = requirement;
        }

        public int getOdd_day() {
            return odd_day;
        }

        public void setOdd_day(int odd_day) {
            this.odd_day = odd_day;
        }

        public int getEven_day() {
            return even_day;
        }

        public void setEven_day(int even_day) {
            this.even_day = even_day;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
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

        public ArrayList<Item> getItemlist() {
            return itemlist;
        }

        public void setItemlist(ArrayList<Item> itemlist) {
            this.itemlist = itemlist;
        }

        public Days getDays() {
            return days;
        }

        public void setDays(Days days) {
            this.days = days;
        }
    }

    public static class Item{
        @SerializedName("prod_id")
        @Expose
        private int prod_id;
        @SerializedName("qty")
        @Expose
        private int qty;

        public int getProd_id() {
            return prod_id;
        }

        public void setProd_id(int prod_id) {
            this.prod_id = prod_id;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }
    }

    public static class Days {
        @SerializedName("1")
        @Expose
        private int sun;
        @SerializedName("2")
        @Expose
        private int mon;
        @SerializedName("3")
        @Expose
        private int tue;
        @SerializedName("4")
        @Expose
        private int wed;
        @SerializedName("5")
        @Expose
        private int thur;
        @SerializedName("6")
        @Expose
        private int fri;
        @SerializedName("7")
        @Expose
        private int sat;

        public int getSun() {
            return sun;
        }

        public void setSun(int sun) {
            this.sun = sun;
        }

        public int getMon() {
            return mon;
        }

        public void setMon(int mon) {
            this.mon = mon;
        }

        public int getTue() {
            return tue;
        }

        public void setTue(int tue) {
            this.tue = tue;
        }

        public int getWed() {
            return wed;
        }

        public void setWed(int wed) {
            this.wed = wed;
        }

        public int getThur() {
            return thur;
        }

        public void setThur(int thur) {
            this.thur = thur;
        }

        public int getFri() {
            return fri;
        }

        public void setFri(int fri) {
            this.fri = fri;
        }

        public int getSat() {
            return sat;
        }

        public void setSat(int sat) {
            this.sat = sat;
        }
    }

    public static class CreateSummeryRes extends BaseRes{
        @SerializedName("subtotal")
        @Expose
        private int subtotal;
        @SerializedName("gst")
        @Expose
        private float gst;
        @SerializedName("delivery_charge")
        @Expose
        private int delivery_charge;
        @SerializedName("payable_amount")
        @Expose
        private float payable_amount;

        public int getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(int subtotal) {
            this.subtotal = subtotal;
        }

        public float getGst() {
            return gst;
        }

        public void setGst(float gst) {
            this.gst = gst;
        }

        public int getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(int delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public float getPayable_amount() {
            return payable_amount;
        }

        public void setPayable_amount(float payable_amount) {
            this.payable_amount = payable_amount;
        }
    }
}
