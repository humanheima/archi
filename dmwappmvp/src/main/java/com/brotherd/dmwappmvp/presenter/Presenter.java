package com.brotherd.dmwappmvp.presenter;

/**
 * Created by dumingwei on 2017/9/3.
 */
public interface Presenter<V> {

    void attachView(V view);

    void detachView();
}
