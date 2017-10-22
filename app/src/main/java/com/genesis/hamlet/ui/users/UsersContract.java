package com.genesis.hamlet.ui.users;

import android.content.Context;

import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.util.mvp.IBasePresenter;
import com.genesis.hamlet.util.mvp.IBaseView;

import java.util.List;

/**
 * The interface that exposes the functionalities of a Photo View and Presenter
 */
interface UsersContract {

    interface View extends IBaseView {
        void showUsers(List<User> users);
        void addUser(User user);
        void updateUser(User user);
        void shouldShowPlaceholderText();
        void remove(User user);
    }

    interface Presenter extends IBasePresenter<View> {
        boolean isConnected();
        void connect(Context context);
        void loadMoreUsers(Context context, int page);
        void interestsUpdated(Context context);
    }
}
