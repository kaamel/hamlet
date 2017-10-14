package com.genesis.hamlet.ui.userdetail;

import com.genesis.hamlet.util.mvp.IBasePresenter;
import com.genesis.hamlet.util.mvp.IBaseView;

/**
 * The interface that exposes the functionalities of a Photo Detail View and Presenter
 */
interface UserDetailContract {

    interface View extends IBaseView {

    }

    interface Presenter extends IBasePresenter<View> {

    }
}
