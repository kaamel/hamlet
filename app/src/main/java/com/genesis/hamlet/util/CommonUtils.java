package com.genesis.hamlet.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.format.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;

/**
 * Created by dipenrana on 10/21/17.
 */

public class CommonUtils {

    public final static int CHAT_USER=0;
    public final static int CHAT_FRIEND =1;
    public final static int CHAT_USER_IMAGE=2;
    public final static int CHAT_USER_NO_IMAGE=2;
    public final static int MESSAGE_TEXT = 0;
    public final static int MESSAGE_IMAGE = 1;

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS,
                    FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        relativeDate.replace("ago","");
        return relativeDate;
    }

    public static String getCityLocation(Context context, double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String city = addresses.get(0).getLocality();
        return  city;

//        String address = addresses.get(0).getAddressLine(0);
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName();
    }








}
