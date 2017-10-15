package com.genesis.hamlet.data.local;


import android.content.Context;

import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import java.util.List;

/**
 * The class for fetching from and storing data into a local SQLite DB on a background thread and
 * returning data via callbacks on the main UI thread
 */
public class LocalDataSource extends DataSource {
    public static LocalDataSource localDataSource;

    public LocalDataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        super(mainUiThread, threadExecutor);
    }

    @Override
    public User getLoggedInUser() {
        return LocalDatabase.getLoggedInUser();
    }

    public static synchronized LocalDataSource getInstance(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        if (localDataSource == null) {
            localDataSource = new LocalDataSource(mainUiThread, threadExecutor);
        }
        return localDataSource;
    }


    @Override
    public void getUsers(Context context, GetUsersCallback getUsersCallback, long maxJoinTime) {
        //// TODO: 10/13/17 turn this into the real method
        getUsersCallback.onSuccess(LocalDatabase.getUsers());

    }

    public void storeUsers(List<User> users) {
        //// TODO: 10/14/17
    }


    @Override
    public void getMMessages(Context context, GetMMessagesCallback getMMessagesCallback, long maxId) {
        //// TODO: 10/13/17
    }

    /**
    // * When data is already available locally, we don't go back to Firebase. The threading and asysnc calls
    // would be managed here while the actual data would be in {@linke LocalDatabase}
    / */


}
