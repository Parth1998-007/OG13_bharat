package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.organicbharat.ui.Base;

public class SendOtpModel {

    public static class SendOtpreq extends BaseReq{
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("email")
        @Expose
        private String email;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class SendOtpRes extends BaseRes{
        @SerializedName("otp")
        @Expose
        private String OTP;

        public String getOTP() {
            return OTP;
        }

        public void setOTP(String OTP) {
            this.OTP = OTP;
        }
    }
}
