package com.genesis.hamlet.ui.users;

import com.genesis.hamlet.util.mvp.IBasePresenter;
import com.genesis.hamlet.util.mvp.IBaseView;

/**
 * The interface that exposes the functionalities of a Photo View and Presenter
 */
interface UsersContract {

    interface View extends IBaseView {

    }

    interface Presenter extends IBasePresenter<View> {

    }
}
