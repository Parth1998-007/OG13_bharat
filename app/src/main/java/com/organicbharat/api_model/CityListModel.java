package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CityListModel {
    public static class CityListRes extends BaseRes{
        @SerializedName("city_list")
        @Expose
        private ArrayList<CityList> cityList;

        public ArrayList<CityList> getCityList() {
            return cityList;
        }

        public void setCityList(ArrayList<CityList> cityList) {
            this.cityList = cityList;
        }
    }

    public static class CityList{
        @SerializedName("city_id")
        @Expose
        private int city_id;
        @SerializedName("city")
        @Expose
        private String city;

        public int getCity_id() {
            return city_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
