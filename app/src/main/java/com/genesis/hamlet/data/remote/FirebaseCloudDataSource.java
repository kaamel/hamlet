package com.genesis.hamlet.data.remote;

import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

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
    public void getUsers(DataSource.GetUsersCallback getUsersCallback, long maxJoinTime) {
        //// TODO: 10/13/17
    }

    @Override
    public void getMMessages(DataSource.GetMMessagesCallback getMMessagesCallback, long maxId) {
        //// TODO: 10/13/17
    }
}
