package com.brotherd.dmwappmvp.presenter;

import android.util.Log;

import com.brotherd.dmwappmvp.ArchiApplication;
import com.brotherd.dmwappmvp.model.GithubService;
import com.brotherd.dmwappmvp.model.User;
import com.brotherd.dmwappmvp.view.RepositoryMvpView;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by dumingwei on 2017/9/3.
 */
public class RepositoryPresenter implements Presenter<RepositoryMvpView> {

    private final String TAG = getClass().getSimpleName();

    private RepositoryMvpView repositoryMvpView;
    private Subscription subscription;

    @Override
    public void attachView(RepositoryMvpView view) {
        this.repositoryMvpView = view;
    }

    @Override
    public void detachView() {
        repositoryMvpView = null;
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void loadOwner(String userUrl) {
        ArchiApplication application = ArchiApplication.get(repositoryMvpView.getContext());
        GithubService service = application.getGithubService();
        subscription = service.userFromUrl(userUrl)
                .subscribeOn(application.defaultSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "Full user data loaded onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "Full user data loaded " + e.getMessage());
                    }

                    @Override
                    public void onNext(User user) {
                        Log.i(TAG, "Full user data loaded " + user);
                        repositoryMvpView.showOwner(user);
                    }
                });
    }
}
