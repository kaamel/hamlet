package com.genesis.hamlet;

import android.app.Application;

import com.genesis.hamlet.data.models.user.User;

public class HamletApplication extends Application {

    static User me;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void setMe(User user) {
        me = user;
    }

    public static User getMe() {
        return me;
    }
}
