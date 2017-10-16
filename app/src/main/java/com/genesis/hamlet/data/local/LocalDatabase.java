package com.genesis.hamlet.data.local;

import android.util.SparseArray;

import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final Map<String, User> usersDB = new HashMap<String, User> (); /*{{
        put("uid1", new User("uid1", "user1"));
        put("uid2", new User("uid2", "user2"));
    }};*/

    private static final List<String> uids = new ArrayList<>();

    /**
     * mMessageId -> mMessage
     */
    private static final SparseArray<MMessage> mMessagesDB = new SparseArray<>();

    public static User getLoggedInUser() {
        if (loggedInUser != null)
            return loggedInUser;
        //// TODO: 10/14/17 uncomment this section when Firebase is setup
        /*
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null)
            return null;
        loggedInUser = UserHelper.extractUser(firebaseUser);
        */
        return loggedInUser;
    }

    public static void storeUsers(List<User> users) {
        synchronized (usersDB) {
            usersDB.clear();
            uids.clear();
            for (User user : users) {
                usersDB.put(user.getUid(), user);
                uids.add(user.getUid());
            }
        }
    }

    public static void addUser(User user) {
        synchronized (usersDB) {
            usersDB.put(user.getUid(), user);
            uids.add(user.getUid());
        }

    }

    public static void removeUser(User user) {
        synchronized (usersDB) {
            usersDB.remove(user.getUid());
            uids.remove(user.getUid());
        }

    }

    public static List<User> getUsers() {
        List<User> users = new ArrayList<>();
        for (int i =0;  i < uids.size(); i++) {
            users.add(usersDB.get(uids.get(i)));
        }
        return users;
    }
}
