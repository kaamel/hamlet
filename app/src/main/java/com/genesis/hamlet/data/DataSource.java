package com.genesis.hamlet.data;

import android.content.Context;

import com.genesis.hamlet.data.local.LocalDataSource;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.data.remote.RemoteDataSource;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import java.util.List;

/**
 * The interface that exposes fetching and storing Firebase storage data through helper methods. This is to be
 * implemented by all data sources such as
 * {@link RemoteDataSource} and
 * {@link LocalDataSource}
 */
public abstract class DataSource {

    protected MainUiThread mainUiThread;
    protected ThreadExecutor threadExecutor;
    public DataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        this.mainUiThread = mainUiThread;
        this.threadExecutor = threadExecutor;
    }

    public interface GetUserCallback {
        void onSuccess(User user);

        void onFailure(Throwable throwable);

        void onNetworkFailure();

    }

    public interface GetUsersCallback {
        void onSuccess(List<User> users);

        void onFailure(Throwable throwable);

        void onNetworkFailure();

    }

    public interface GetMMessagesCallback {

        void onSuccess(List<MMessage> mMessages);

        void onFailure(Throwable throwable);

        void onNetworkFailure();

    }

    public abstract User getLoggedInUser();
    public abstract void getUsers(Context context, GetUsersCallback getUsersCallback, long maxJoinTime);
    public abstract void getMMessages(Context context, GetMMessagesCallback getMMessagesCallback, long maxId);
}
