package com.genesis.hamlet.data.models.filter;

/**
 * Created by kaamel on 10/13/17.
 */

public class Interests {

    private static final String[] interestTopics = {
            "Art",
            "Sports",
            "Emgergency",
            "Movies",
            "Food",
            "Health & Fitness",
            "Child Care",
            "Ride Sharing",
            "Entertainment",
            "Automobile",
            "Technology",
            "Science"
    };

    private String introTitle;
    private String introDetail;

    boolean[] interests = new boolean[interestTopics.length];

    public Interests(String introTitle, String introDetail, boolean[] interests) {
        this.introTitle = introTitle;
        this.introDetail = introDetail;
        this.interests = interests;
    }

    public static String[] getInterestTopics() {
        return interestTopics;
    }

    public String getIntroTitle() {
        return introTitle;
    }

    public void setIntroTitle(String introTitle) {
        this.introTitle = introTitle;
    }

    public String getIntroDetail() {
        return introDetail;
    }

    public void setIntroDetail(String introDetail) {
        this.introDetail = introDetail;
    }

    public boolean[] getInterests() {
        return interests;
    }

    public void setInterests(boolean[] interests) {
        this.interests = interests;
    }
}
