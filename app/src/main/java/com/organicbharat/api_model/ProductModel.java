package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.organicbharat.utils.AppLog;

import java.util.ArrayList;

public class ProductModel {

    public static class ProductListReq extends BaseReq{
        private static final String TAG = "ProductListReq";
        @SerializedName("user_id")
        @Expose
        private int user_id;
        @SerializedName("category_id")
        @Expose
        private String category_id;
        @SerializedName("subscription")
        @Expose
        private int subscription;
        @SerializedName("page")
        @Expose
        private int page;

        public ProductListReq(int user_id, String category_id, int subscription, int page) {
            this.user_id = user_id;
            this.category_id = category_id;
            this.subscription = subscription;
            this.page = page;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public int getSubscription() {
            return subscription;
        }

        public void setSubscription(int subscription) {
            this.subscription = subscription;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }
    }

    public static class ProductListRes extends BaseRes{
        @SerializedName("product_list")
        @Expose
        private ArrayList<ProductData> product_list;
        @SerializedName("slots")
        @Expose
        private ArrayList<Slot> slots;

        public ArrayList<ProductData> getProduct_list() {
            return product_list;
        }

        public void setProduct_list(ArrayList<ProductData> product_list) {
            this.product_list = product_list;
        }

        public ArrayList<Slot> getSlots() {
            return slots;
        }

        public void setSlots(ArrayList<Slot> slots) {
            this.slots = slots;
        }
    }

    public static class Slot{
        @SerializedName("time_slots")
        @Expose
        private String time_slots;

        public String getTime_slots() {
            return time_slots;
        }

        public void setTime_slots(String time_slots) {
            this.time_slots = time_slots;
        }
    }

    public static class ProductData {
        @SerializedName("order_id")
        @Expose
        private int order_id;
        @SerializedName("product_id")
        @Expose
        private int product_id;
        @SerializedName("product")
        @Expose
        private String product;
        @SerializedName("size")
        @Expose
        private String size;
        @SerializedName("rate")
        @Expose
        private String rate;
        @SerializedName("discounted_rate")
        @Expose
        private String discounted_rate;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("qty")
        @Expose
        private int qty;
        @SerializedName("availibility")
        @Expose
        private int availibility;
        @SerializedName("delivery_time")
        @Expose
        private String delivery_time;
        @SerializedName("prod_image")
        @Expose
        private String prod_image;
        @SerializedName("Count")
        @Expose
        private int count;
        @SerializedName("isavailable")
        @Expose
        private boolean isavailable;
        @SerializedName("item_in_cart")
        @Expose
        private int item_in_cart;
        @SerializedName("is_subscription")
        @Expose
        private int is_subscription;
        @SerializedName("start_date")
        @Expose
        private String start_date;
        @SerializedName("end_date")
        @Expose
        private String end_date;

        @SerializedName("is_paused")
        @Expose
        private int is_paused;

        public int getIs_paused() {
            return is_paused;
        }

        public void setIs_paused(int is_paused) {
            this.is_paused = is_paused;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getDiscounted_rate() {
            return discounted_rate;
        }

        public void setDiscounted_rate(String discounted_rate) {
            this.discounted_rate = discounted_rate;
        }

        public ProductData(int product_id, String product, String size, String rate, String description, int qty, int availibility, String delivery_time, String prod_image, int item_in_cart) {
            this.product_id = product_id;
            this.product = product;
            this.size = size;
            this.rate = rate;
            this.description = description;
            this.qty = qty;
            this.availibility = availibility;
            this.delivery_time = delivery_time;
            this.prod_image = prod_image;
            this.item_in_cart = item_in_cart;
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

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public int getAvailibility() {
            return availibility;
        }

        public void setAvailibility(int availibility) {
            this.availibility = availibility;
        }

        public String getDelivery_time() {
            return delivery_time;
        }

        public void setDelivery_time(String delivery_time) {
            this.delivery_time = delivery_time;
        }

        public String getProd_image() {
            return prod_image;
        }

        public void setProd_image(String prod_image) {
            this.prod_image = prod_image;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isavailable() {
            return isavailable;
        }

        public void isavailable(boolean isavailable) {
            this.isavailable = isavailable;
        }

        public int getItem_in_cart() {
            return item_in_cart;
        }

        public void setItem_in_cart(int item_in_cart) {
            this.item_in_cart = item_in_cart;
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
    }

    public static class ProductDetailsRes extends BaseRes{
        @SerializedName("product_id")
        @Expose
        private int product_id;
        @SerializedName("is_subscription")
        @Expose
        private int issubscription;
        @SerializedName("item_in_cart")
        @Expose
        private int item_in_cart;
        @SerializedName("product")
        @Expose
        private String product;
        @SerializedName("size")
        @Expose
        private String size;
        @SerializedName("rate")
        @Expose
        private String rate;

        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("qty")
        @Expose
        private int qty;
        @SerializedName("availibility")
        @Expose
        private int availibility;
        @SerializedName("delivery_time")
        @Expose
        private String delivery_time;
        @SerializedName("product_image")
        @Expose
        private String product_image;
        @SerializedName("discounted_rate")
        @Expose
        private String discounted_rate;

        public String getDiscounted_rate() {
            return discounted_rate;
        }

        public void setDiscounted_rate(String discounted_rate) {
            this.discounted_rate = discounted_rate;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getIssubscription() {
            return issubscription;
        }

        public void setIssubscription(int issubscription) {
            this.issubscription = issubscription;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public int getAvailibility() {
            return availibility;
        }

        public void setAvailibility(int availibility) {
            this.availibility = availibility;
        }

        public String getDelivery_time() {
            return delivery_time;
        }

        public void setDelivery_time(String delivery_time) {
            this.delivery_time = delivery_time;
        }

        public String getProduct_image() {
            return product_image;
        }

        public void setProduct_image(String product_image) {
            this.product_image = product_image;
        }

        public int getItem_in_cart() {
            return item_in_cart;
        }

        public void setItem_in_cart(int item_in_cart) {
            this.item_in_cart = item_in_cart;
        }
    }

    public static class ProductDetailsReq extends BaseReq{
        @SerializedName("user_id")
        @Expose
        private int user_id;
        @SerializedName("product_id")
        @Expose
        private String product_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }
    }
}
