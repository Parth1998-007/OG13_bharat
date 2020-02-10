package com.organicbharat.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchModel {
        public static class SearchReq extends BaseReq{
            @SerializedName("search")
            @Expose
            private String search;
            @SerializedName("user_id")
            @Expose
            private int user_id;

            public String getSearch() {
                return search;
            }

            public void setSearch(String search) {
                this.search = search;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }
        }
}
