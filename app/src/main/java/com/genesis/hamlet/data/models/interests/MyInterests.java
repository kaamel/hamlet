package com.genesis.hamlet.data.models.interests;

/**
 * Created by kaamel on 10/13/17.
 */

public class MyInterests extends Interests {


    private static MyInterests instance;
    private static String myUid;

    private  static Interests myInterests;

    public static synchronized MyInterests getInstance() {
        if (instance == null) {
            instance = new MyInterests();
        }
        return instance;
    }

    private MyInterests() {
    }

    public String getMyUid() {
        return myUid;
    }

    public void setMyUid(String uId) {
        myUid = uId;
    }

}
