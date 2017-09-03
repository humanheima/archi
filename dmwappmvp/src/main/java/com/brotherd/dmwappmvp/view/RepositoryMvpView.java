package com.brotherd.dmwappmvp.view;

import com.brotherd.dmwappmvp.model.User;

/**
 * Created by dumingwei on 2017/9/3.
 */
public interface RepositoryMvpView extends MvpView {

    void showOwner(final User owner);
}
