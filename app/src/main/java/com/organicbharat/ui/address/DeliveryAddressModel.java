package com.organicbharat.ui.address;

import java.io.Serializable;

public class DeliveryAddressModel implements Serializable {
    private String locality;
    private String flat_no;
    private String street_name;
    private String city_name;
    private String landmark;
    private String pincode;
    private String email;
    private String contact;

    public DeliveryAddressModel(String locality, String flat_no, String street_name, String city_name, String landmark, String pincode, String email, String contact) {
        this.locality = locality;
        this.flat_no = flat_no;
        this.street_name = street_name;
        this.city_name = city_name;
        this.landmark = landmark;
        this.pincode = pincode;
        this.email = email;
        this.contact=contact;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocality() {
        return locality;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public String getStreet_name() {
        return street_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public String getEmail() {
        return email;
    }
}
