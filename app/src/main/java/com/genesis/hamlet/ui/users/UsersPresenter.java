package com.genesis.hamlet.ui.users;

import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.util.mvp.BasePresenter;

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
}
