package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryModel {

    public static class CategoryReq extends BaseReq{
        @SerializedName("user_id")
        @Expose
        private int user_id;
        @SerializedName("subscription")
        @Expose
        private int subscription;
        @SerializedName("page")
        @Expose
        private int page;

        public CategoryReq() {
        }

        public CategoryReq(int user_id, int subscription, int page) {
            this.user_id = user_id;
            this.subscription = subscription;
            this.page = page;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
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
    public static class CategoryRes extends BaseRes{
        @SerializedName("category_list")
        @Expose
        private ArrayList<CategoryData> categoryList;

        @SerializedName("config")
        @Expose
        private Config ConfigData;

        public ArrayList<CategoryData> getCategoryList() {
            return categoryList;
        }

        public void setCategoryList(ArrayList<CategoryData> categoryList) {
            this.categoryList = categoryList;
        }

        public Config getConfigData() {
            return ConfigData;
        }

        public void setConfigData(Config configData) {
            ConfigData = configData;
        }
    }

    public static class Config{
        @SerializedName("min_amount")
        @Expose
        private String min_amount;
        @SerializedName("delivery_charge")
        @Expose
        private String delivery_charge;

        public String getMin_amount() {
            return min_amount;
        }

        public void setMin_amount(String min_amount) {
            this.min_amount = min_amount;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }
    }

    public static class CategoryData {
        @SerializedName("cat_id")
        @Expose
        private int cat_id;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("category_image")
        @Expose
        private String category_image;

        public int getCat_id() {
            return cat_id;
        }

        public void setCat_id(int cat_id) {
            this.cat_id = cat_id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCategory_image() {
            return category_image;
        }

        public void setCategory_image(String category_image) {
            this.category_image = category_image;
        }
    }
}
