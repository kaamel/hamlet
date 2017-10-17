
package com.genesis.hamlet.data.models.user;

import org.parceler.Parcel;

@Parcel(value = Parcel.Serialization.BEAN, analyze = {User.class})
public class User {
    private String uid;
    private String displayName;
    private String photoUrl;
    private String email;
    private String phoneNumber;



    private String tagline;
    private long lat;
    private long lon;
    private long locationUpdateTime;

    //// TODO: 10/14/17 replace this constructor with a real one

    public User(String name, String uid) {
        displayName = name;
        this.uid = uid;
    }

    public User() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public long getLocationUpdateTime() {
        return locationUpdateTime;
    }

    public void setLocationUpdateTime(long locationUpdateTime) {
        this.locationUpdateTime = locationUpdateTime;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }
}
