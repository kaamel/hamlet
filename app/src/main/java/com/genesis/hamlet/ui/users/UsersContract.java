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
        void showUser(User user);
        void shouldShowPlaceholderText();

        void remove(User user);
    }

    interface Presenter extends IBasePresenter<View> {
        void getUsers(Context context, int page);
    }
}
