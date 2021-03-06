package com.genesis.hamlet.ui.users;

import android.content.Context;

import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.mvp.BasePresenter;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import java.util.List;

/**
 * The Presenter that fetches photo data by calling {@link DataRepository} at the request of
 * its View "{@link UsersContract.View}", and then delivers the data back to
 * its View.
 * The presenter also calls other relevant methods of its View such as for
 * showing/hiding progress bar and for showing Toast messages.
 * The Presenter subscribes to its View lifecycle by allowing
 * the View to call the Presenter's {@link #onViewActive(Object)} and {@link #onViewInactive()}
 * to reference/unreference its View. This allows its View to be GCed and prevents memory leaks.
 * The Presenter also checks if its View is active before calling any of its methods.
 */
public class UsersPresenter extends BasePresenter<UsersContract.View> implements
        UsersContract.Presenter {

    private DataRepository dataRepository;
    private ThreadExecutor threadExecutor;
    private MainUiThread mainUiThread;

    public UsersPresenter(UsersContract.View view, DataRepository dataRepository,
                          ThreadExecutor threadExecutor, MainUiThread mainUiThread) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.threadExecutor = threadExecutor;
        this.mainUiThread = mainUiThread;
    }

    @Override
    public boolean isConnected() {
        return dataRepository.isConnected();
    }

    @Override
    public void connect(Context context) {
        if (dataRepository.isConnected())
            return;
        view.setProgressBar(true);

        dataRepository.connectRemote(context, new DataSource.OnUsersCallback() {
            @Override
            public void onSuccess(List<User> users) {
                if (view != null) {
                    view.setProgressBar(false);
                    if (users == null || users.size() == 0) {
                        return;
                    }
                    view.showUsers(users);
                    view.shouldShowPlaceholderText();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (view != null) {
                    view.setProgressBar(false);
                    //// TODO: 10/14/17 set error message here
                    view.showToastMessage("Something went wrong");
                    view.shouldShowPlaceholderText();
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    //// TODO: 10/14/17 set the network error message here
                    view.showToastMessage("Something wrong with network");
                    view.shouldShowPlaceholderText();
                }
            }

            @Override
            public void onNewUserJoined(User user) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.addUser(user);
                }
            }

            @Override
            public void onUserLeft(User user) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.remove(user);
                }
            }

            @Override
            public void onUserUpdate(User user) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.updateUser(user);
                }
            }
        }, 0);
    }

    @Override
    public void loadMoreUsers(Context context, int page) {
        if (dataRepository.isConnected()) {
            if (view != null)
                view.setProgressBar(false);
            return;
        }
        connect(context);
    }

    @Override
    public void interestsUpdated(Context context) {
        if (dataRepository.isConnected()) {
            if (view != null)
                view.setProgressBar(false);
            dataRepository.updateUserInterests();
            return;
        }
        connect(context);
    }
}
