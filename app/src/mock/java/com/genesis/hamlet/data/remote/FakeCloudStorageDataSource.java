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
    public void getUsers(final Context context, final OnUserCallback onUserCallback, long maxJoinTime) {

        final Handler handler = new Handler();
        final List<User> users = new ArrayList<>();

        final User user1 = new User("John K", "Looking for someone to play tennis with");
        user1.setIntroTitle("love walking");
        user1.setPhotoUrl("https://scontent.fsnc1-1.fna.fbcdn.net/v/t1.0-9/11880524_10207583816890476_7006713471158652602_n.jpg?oh=1d95b3dce9f849b86cc9faec563757c8&oe=5A785E69");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                users.add(user1);

                User user2 = new User("Kelly", "My 5 year old son wants to play in the park with same age kid");
                user2.setIntroTitle("hate walking!");
                user2.setPhotoUrl("https://scontent.fsnc1-1.fna.fbcdn.net/v/t1.0-9/12472243_10153792983241605_1589806110665512082_n.jpg?oh=f7e57de11f58977d74001b49b615ba25&oe=5A3BE974");
                users.add(user2);

                onUserCallback.onSuccess(users);
            }
        }, 100);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                User user3 = new User("Fay", "Need a ride to Mountain View");
                user3.setIntroTitle("Need a ride!");
                user3.setPhotoUrl("https://scontent.fsnc1-1.fna.fbcdn.net/v/t1.0-9/12472243_10153792983241605_1589806110665512082_n.jpg?oh=f7e57de11f58977d74001b49b615ba25&oe=5A3BE974");

                onUserCallback.onNewUserJoined(user3);
            }
        }, 2100);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onUserCallback.onUserLeft(user1);
            }
        }, 8000);
    }

    @Override
    public void storeUsers(List<User> users) {
        //We don't store users on remote
    }

    @Override
    public void getMMessages(Context context, OnMMessagesCallback onMMessagesCallback, long maxId) {
        //// TODO: 10/13/17
    }
}
