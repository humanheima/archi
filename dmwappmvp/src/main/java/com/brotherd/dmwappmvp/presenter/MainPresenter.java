package com.brotherd.dmwappmvp.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.brotherd.dmwappmvp.ArchiApplication;
import com.brotherd.dmwappmvp.R;
import com.brotherd.dmwappmvp.model.GithubService;
import com.brotherd.dmwappmvp.model.Repository;
import com.brotherd.dmwappmvp.view.MainMvpView;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by dumingwei on 2017/9/3.
 */
public class MainPresenter implements Presenter<MainMvpView> {


    private final String TAG = getClass().getSimpleName();

    private MainMvpView mainMvpView;
    private Subscription subscription;
    private List<Repository> repositories;

    @Override
    public void attachView(MainMvpView view) {
        this.mainMvpView = view;
    }

    @Override
    public void detachView() {
        this.mainMvpView = null;
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    /**
     * 这就相当于一个 use case
     * @param userName
     */
    public void loadRepositories(String userName) {
        if (TextUtils.isEmpty(userName.trim())) {
            return;
        }
        mainMvpView.showProgressIndicator();
        if (subscription != null) {
            subscription.unsubscribe();
        }
        ArchiApplication application = ArchiApplication.get(mainMvpView.getContext());
        GithubService service = application.getGithubService();
        subscription = service.publicRepositories(userName)
                .subscribeOn(application.defaultSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repository>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "Repos loaded " + repositories);
                        if (!repositories.isEmpty()) {
                            mainMvpView.showRepositories(repositories);
                        } else {
                            mainMvpView.showMessage(R.string.text_empty_repos);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error loading GitHub repos ", e);
                        if (isHttp404(e)) {
                            mainMvpView.showMessage(R.string.error_username_not_found);
                        } else {
                            mainMvpView.showMessage(R.string.error_loading_repos);
                        }
                    }

                    @Override
                    public void onNext(List<Repository> repositories) {
                        MainPresenter.this.repositories = repositories;
                    }
                });

    }

    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }

}
