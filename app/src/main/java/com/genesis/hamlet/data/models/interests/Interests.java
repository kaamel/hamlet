package com.genesis.hamlet.data.models.interests;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaamel on 10/13/17.
 */

public class Interests {

    public static final String[] interestTopics = {
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

    private static Interests instance;

    private String nickName = "";
    private String introTitle = "";
    private String introDetail = "";

    private List<Boolean> interestCats = new ArrayList<>();// boolean[interestTopics.length];

    @Exclude
    private boolean changed = false;

    public static synchronized Interests getInstance() {
        if (instance == null) {
            instance = new Interests();
        }

        return instance;
    }

    private Interests() {
        clearInterests();
    }

    @Exclude
    public boolean isIncomplete() {
        return !((nickName != null && nickName.trim().length() > 0)
                && (interestTopics != null)
                && (interestTopics.length > 0)
                && (introTitle != null && introTitle.trim().length() > 0));
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

    public List getInterestCats() {
        return interestCats;
    }

    public void setInterestCats(List interestCats) {
        this.interestCats = interestCats;
    }

    public boolean getInterest(int pos) {
        return interestCats.get(pos);
    }

    public void setInterest(boolean interested, int pos) {
        interestCats.set(pos, interested);
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void clearInterests() {
        for (String item: interestTopics) {
            interestCats.add(false);
        }
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean isChanged() {
        return changed;
    }

    @Exclude
    public boolean[] getInterests() {
        boolean[] interests = new boolean[interestTopics.length];
        for (int i=0; i< interestTopics.length; i++)
            interests[i] = interestCats.get(i);
        return interests;
    }
}
