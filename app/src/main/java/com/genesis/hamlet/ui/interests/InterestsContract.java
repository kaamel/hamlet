package com.genesis.hamlet.ui.interests;

import com.genesis.hamlet.data.models.interests.Interests;
import com.genesis.hamlet.util.mvp.IBasePresenter;
import com.genesis.hamlet.util.mvp.IBaseView;

/**
 * Created by kaamel on 10/17/17.
 */

interface InterestsContract {
    interface View extends IBaseView {
        Interests getInterests();

        void resetForm();
    }

    interface Presenter extends IBasePresenter<InterestsContract.View> {
        Interests getInterests();
    }
}
