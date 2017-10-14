package com.genesis.hamlet.data.remote;

import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

/**
 * Created by sshah on 3/28/17.
 */

public class FakeCloudStorageDataSource extends DataSource {

    //everything here is fake so we can do testing before firebase is setup
    public static FakeCloudStorageDataSource fakeCloudStorageDataSource;

    public FakeCloudStorageDataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        super(mainUiThread, threadExecutor);
    }

    public static synchronized FakeCloudStorageDataSource getInstance(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        if (fakeCloudStorageDataSource == null) {
            fakeCloudStorageDataSource = new FakeCloudStorageDataSource(mainUiThread, threadExecutor);
        }
        return fakeCloudStorageDataSource;
    }

    @Override
    public User getLoggedInUser() {
        //// TODO: 10/13/17
        return null;
    }

    @Override
    public void getUsers(GetUsersCallback getUsersCallback, long maxJoinTime) {
        //// TODO: 10/13/17
    }

    @Override
    public void getMMessages(GetMMessagesCallback getMMessagesCallback, long maxId) {
        //// TODO: 10/13/17
    }
}
