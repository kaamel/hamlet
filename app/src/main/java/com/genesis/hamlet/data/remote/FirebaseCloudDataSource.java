package com.genesis.hamlet.data.remote;

import android.content.Context;

import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import java.util.List;

/**
 * Created by kaamel on 10/13/17.
 */

public class FirebaseCloudDataSource extends DataSource {
    public static FirebaseCloudDataSource firebaseCloudDataSource;

    public FirebaseCloudDataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        super(mainUiThread, threadExecutor);
    }

    public static synchronized FirebaseCloudDataSource getInstance(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        if (firebaseCloudDataSource == null) {
            firebaseCloudDataSource = new FirebaseCloudDataSource(mainUiThread, threadExecutor);
        }
        return firebaseCloudDataSource;
    }

    @Override
    public User getLoggedInUser() {
        //// TODO: 10/13/17
        return null;
    }

    @Override
    public void getUsers(Context context, DataSource.GetUsersCallback getUsersCallback, long maxJoinTime) {
        //// TODO: 10/15/17 Set up the firebase, call back the initial list of users and call back as users join/leave
        //// TODO: 10/15/17 I also think we need to add the filter to the method
    }

    @Override
    public void storeUsers(List<User> users) {
        //// TODO: 10/15/17 I think we should never use thib for firebase but ...
    }

    @Override
    public void getMMessages(Context context, DataSource.GetMMessagesCallback getMMessagesCallback, long maxId) {
        //// TODO: 10/13/17
    }
}
