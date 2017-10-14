package com.genesis.hamlet.ui.userdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.genesis.hamlet.ui.users.UsersFragment;
import com.genesis.hamlet.util.mvp.BaseView;

/**
 * The {@link Fragment} that receives photo data from
 * {@link UsersFragment}
 * via a {@link Bundle} and comment data from its
 * {@link UserDetailContract.Presenter}. It then renders the photo and its list of comments.
 */
public class UserDetailFragment extends BaseView implements UserDetailContract.View {

}
