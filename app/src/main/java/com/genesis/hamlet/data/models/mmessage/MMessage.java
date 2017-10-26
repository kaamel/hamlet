
package com.genesis.hamlet.data.models.mmessage;

public class MMessage {

    String id;



    String displayName;
    String userImage;
    String text;
    String imageUrl;
    String audioUrl;
    String videoUrl;

    long locationLat;
    long locationLon;
    long locationTime;

    long createTime;

    String senderUid;

    public MMessage() {

    }


    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(long locationLat) {
        this.locationLat = locationLat;
    }

    public long getLocationLon() {
        return locationLon;
    }

    public void setLocationLon(long locationLon) {
        this.locationLon = locationLon;
    }

    public long getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(long locationTime) {
        this.locationTime = locationTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getId() {
        return id;
    }

}
