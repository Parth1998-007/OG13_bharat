package com.organicbharat.api_model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddressModel {
    public static class AddressListRes extends BaseRes{
        @SerializedName("data")
        @Expose
        private ArrayList<Data> dataList;

        public ArrayList<Data> getDataList() {
            return dataList;
        }

        public void setDataList(ArrayList<Data> dataList) {
            this.dataList = dataList;
        }
    }

    public static class Data{
        @SerializedName("address_id")
        @Expose
        private int address_id;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("area")
        @Expose
        private String area;
        @SerializedName("flat_no")
        @Expose
        private String flat_no;
        @SerializedName("street")
        @Expose
        private String street;
        @SerializedName("landmark")
        @Expose
        private String landmark;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lng")
        @Expose
        private String lng;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("pincode")
        @Expose
        private String pincode;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("city_id")
        @Expose
        private int city_id;
        @SerializedName("is_default")
        @Expose
        private int isdefault;
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public int getAddress_id() {
            return address_id;
        }

        public void setAddress_id(int address_id) {
            this.address_id = address_id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getFlat_no() {
            return flat_no;
        }

        public void setFlat_no(String flat_no) {
            this.flat_no = flat_no;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getLandmark() {
            return landmark;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getCity_id() {
            return city_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public int getIsdefault() {
            return isdefault;
        }

        public void setIsdefault(int isdefault) {
            this.isdefault = isdefault;
        }
    }

    public static class AddAddressReq extends BaseReq{
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("user_id")
        @Expose
        private int user_id;
        @SerializedName("city")
        @Expose
        private int city;
        @SerializedName("lng")
        @Expose
        private String lng;
        @SerializedName("zipcode")
        @Expose
        private String zipcode;
        @SerializedName("landmark")
        @Expose
        private String landmark;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("flat_no")
        @Expose
        private String flat_no;
        @SerializedName("street")
        @Expose
        private String street;
        @SerializedName("area")
        @Expose
        private String area;
        @SerializedName("email")
        @Expose
        private String email;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getLandmark() {
            return landmark;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getFlat_no() {
            return flat_no;
        }

        public void setFlat_no(String flat_no) {
            this.flat_no = flat_no;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
           return new Gson().toJson(this);

        }
    }

    public static class EditAddressReq extends BaseReq{

        @SerializedName("address_id")
        @Expose
        private String address_id;
        @SerializedName("user_id")
        @Expose
        private int user_id;
        @SerializedName("city")
        @Expose
        private int city;
        @SerializedName("lng")
        @Expose
        private String lng;
        @SerializedName("zipcode")
        @Expose
        private String zipcode;
        @SerializedName("landmark")
        @Expose
        private String landmark;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("flat_no")
        @Expose
        private String flat_no;
        @SerializedName("street")
        @Expose
        private String street;
        @SerializedName("area")
        @Expose
        private String area;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("is_default")
        @Expose
        private int isdefault;
        public String getAddress_id() {
            return address_id;
        }

        public void setAddress_id(String address_id) {
            this.address_id = address_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getLandmark() {
            return landmark;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getFlat_no() {
            return flat_no;
        }

        public void setFlat_no(String flat_no) {
            this.flat_no = flat_no;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getIsdefault() {
            return isdefault;
        }

        public void setIsdefault(int isdefault) {
            this.isdefault = isdefault;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);

        }
    }
}
