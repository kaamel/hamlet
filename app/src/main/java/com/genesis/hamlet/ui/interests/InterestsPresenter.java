package com.genesis.hamlet.ui.interests;

import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.models.interests.Interests;
import com.genesis.hamlet.util.mvp.BasePresenter;

/**
 * Created by kaamel on 10/17/17.
 */

public class InterestsPresenter extends BasePresenter<InterestsContract.View> implements
        InterestsContract.Presenter {

    private DataRepository dataRepository;

    public InterestsPresenter(InterestsContract.View view) {
        this.view = view;
    }

    @Override
    public Interests getInterests() {
        if (view == null) {
            return null;
        }

        return  view.getInterests();
    }
}
