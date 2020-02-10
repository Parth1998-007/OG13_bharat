package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OfferListModel {

    public static class OfferListRes extends BaseRes{

        @SerializedName("offer_list")
        @Expose
        private ArrayList<OfferList> offerlist;

        public ArrayList<OfferList> getOfferlist() {
            return offerlist;
        }

        public void setOfferlist(ArrayList<OfferList> offerlist) {
            this.offerlist = offerlist;
        }
    }

    public static class OfferList{
        @SerializedName("offer_id")
        @Expose
        private int offer_id;
        @SerializedName("offer")
        @Expose
        private String offer;

        public int getOffer_id() {
            return offer_id;
        }

        public void setOffer_id(int offer_id) {
            this.offer_id = offer_id;
        }

        public String getOffer() {
            return offer;
        }

        public void setOffer(String offer) {
            this.offer = offer;
        }
    }
}
