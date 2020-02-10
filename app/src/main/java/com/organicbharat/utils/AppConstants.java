package com.organicbharat.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AppConstants
{
    public static final int LOGIN_TYPE_NORMAL = 1;

    //PAYTM PARAMETERS
    public static final String PAYTM_MID = "svtIWt29551751638864";
    public static final String PAYTM_CALLBACK = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";
    public static final String PAYTM_WEBSITE = "DEFAULT";

    public static final String URL_FOR_PAYTM = "http://13.127.149.72/organic_bharat/Paytm_v1/generateChecksum.php";

    public static String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
