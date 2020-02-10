package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AreaListModel {
    public static class AreaListRes extends BaseRes{
        @SerializedName("area_list")
        @Expose
        private ArrayList<AreaList> areaList;

        public ArrayList<AreaList> getAreaList() {
            return areaList;
        }

        public void setAreaList(ArrayList<AreaList> areaList) {
            this.areaList = areaList;
        }
    }

    public static class AreaList{
        @SerializedName("area_id")
        @Expose
        private int area_id;
        @SerializedName("area")
        @Expose
        private String area;
        @SerializedName("pincode")
        @Expose
        private String pincode;

        public int getArea_id() {
            return area_id;
        }

        public void setArea_id(int area_id) {
            this.area_id = area_id;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }
    }
}
