package com.genesis.hamlet.data.remote;

import android.content.Context;
import android.os.Handler;

import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import java.util.ArrayList;
import java.util.List;

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
    public void getUsers(final Context context, final GetUsersCallback getUsersCallback, long maxJoinTime) {

        final Handler handler = new Handler();
        final List<User> users = new ArrayList<>();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                users.add(new User("John K", "Looking for someone to play tennis with"));
                users.add(new User("Kelly", "My 5 year old son wants to play in the park with same age kid"));
                getUsersCallback.onSuccess(users);
            }
        }, 100);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUsersCallback.onNewUserJoined(new User("Fay", "Need a ride to Mountain View"));
            }
        }, 2100);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUsersCallback.onNewUserJoined(new User("Brad", "I love beer. Anyone wants to join me?"));
            }
        }, 4000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUsersCallback.onUserLeft(new User("John K", "Looking for someone to play tennis with"));
            }
        }, 8000);
    }

    @Override
    public void storeUsers(List<User> users) {
        //We don't store users on remote
    }

    @Override
    public void getMMessages(Context context, GetMMessagesCallback getMMessagesCallback, long maxId) {
        //// TODO: 10/13/17
    }
}
