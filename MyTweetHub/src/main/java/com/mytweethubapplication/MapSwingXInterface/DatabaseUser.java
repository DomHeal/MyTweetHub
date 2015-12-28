package com.mytweethubapplication.MapSwingXInterface;

/**
 * Created by Dominic on 28-Dec-15.
 */
public class DatabaseUser {

    private String pictureURL;
    private String screenName;
    private String name;
    private String createdAt;
    private String tweetType;
    private String longitude;
    private String latitude;
    private String text;
    private String likes;
    private String retweets;

    public String getPictureURL() {
        return pictureURL;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTweetType() {
        return tweetType;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLikes() {
        return likes;
    }

    public String getRetweets() {
        return retweets;
    }

    public String getText() {
        return text;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setTweetType(String tweetType) {
        this.tweetType = tweetType;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public void setRetweets(String retweets) {
        this.retweets = retweets;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getName() {
        return name;
    }
}
