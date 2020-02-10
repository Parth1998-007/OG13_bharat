package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.organicbharat.ui.Base;

import java.util.ArrayList;

public class ProductAvailabilityModel {

    public static class ProductAvailabilityReq extends BaseReq{
        @SerializedName("products")
        @Expose
        private ArrayList<ProductAvailabilityModel.Products> productlist;

        @SerializedName("user_id")
        @Expose
        private int user_id;

        public ArrayList<ProductAvailabilityModel.Products> getProductlist() {
            return productlist;
        }

        public void setProductlist(ArrayList<ProductAvailabilityModel.Products> productlist) {
            this.productlist = productlist;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

    }

    public static class ProductAvailabilityRes extends BaseRes {
        @SerializedName("products")
        public  ArrayList<ProductResListData> productsResponseList;


        public ArrayList<ProductResListData> getProductsResponseList() {
            return productsResponseList;
        }

        public void setProductsResponseList(ArrayList<ProductResListData> productsResponseList) {
            this.productsResponseList = productsResponseList;
        }
    }

    public static class ProductResListData {

        @SerializedName("id")
        @Expose
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Products {

        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("qty")
        @Expose
        private int qty;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }
    }


}
