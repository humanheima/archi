package com.brotherd.dmwappmvp.view;

import com.brotherd.dmwappmvp.model.Repository;

import java.util.List;

/**
 * Created by dumingwei on 2017/9/3.
 */
public interface MainMvpView extends MvpView {

    void showRepositories(List<Repository> repositories);

    void showMessage(int stringId);

    void showProgressIndicator();

}
