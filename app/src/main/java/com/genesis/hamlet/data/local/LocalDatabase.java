package com.genesis.hamlet.data.local;

import android.support.v4.util.LongSparseArray;
import android.util.SparseArray;

import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * The class defining the local datastore
 */
public class LocalDatabase {
    //
    // I don't expect our project to need a database - if it did, this is where we would define it
    // @Database(name = LocalDatabase.NAME, version = LocalDatabase.VERSION)
    // public class LocalDatabase {
    //    public static final String NAME = "HamletDB";
    //
    //    public static final int VERSION = 1;
    // }
    // For our app we can just use Map and/or static fields, I think
    //

    private static User loggedInUser;

    /**
     * userId -> user
     */
    private static final LongSparseArray<User> usersDB = new LongSparseArray<>();

    /**
     * mMessageId -> mMessage
     */
    private static final SparseArray<MMessage> mMessagesDB = new SparseArray<>();

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static void setUsers(List<User> users) {
        usersDB.clear();
        for (User user: users) {
            usersDB.append(user.getId(), user);
        }
    }

    public static List<User> getUsers() {
        List<User> us = new ArrayList<>();
        int size =usersDB.size();
        for (int i = 0; i< size; i++) {
            us.add(usersDB.valueAt(i));
        }
        return us;
    }
}
