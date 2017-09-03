package com.brotherd.dmwappmvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brotherd.dmwappmvp.R;
import com.brotherd.dmwappmvp.model.Repository;
import com.brotherd.dmwappmvp.model.User;
import com.brotherd.dmwappmvp.presenter.RepositoryPresenter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RepositoryActivity extends AppCompatActivity implements RepositoryMvpView {

    private static final String EXTRA_REPOSITORY = "EXTRA_REPOSITORY";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.text_description)
    TextView textDescription;
    @BindView(R.id.text_home_page)
    TextView textHomePage;
    @BindView(R.id.text_language)
    TextView textLanguage;
    @BindView(R.id.text_fork)
    TextView textFork;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.img_circle)
    CircleImageView imgCircle;
    @BindView(R.id.text_owner)
    TextView textOwner;
    @BindView(R.id.text_email)
    TextView textEmail;
    @BindView(R.id.text_address)
    TextView textAddress;

    private Repository repository;
    private RepositoryPresenter presenter;

    public static void launch(Context context, Repository repository) {
        Intent starter = new Intent(context, RepositoryActivity.class);
        starter.putExtra(EXTRA_REPOSITORY, repository);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RepositoryPresenter();
        presenter.attachView(this);
        setContentView(R.layout.activity_repository);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        repository = getIntent().getParcelableExtra(EXTRA_REPOSITORY);
        bindRepositoryData(repository);
        presenter.loadOwner(repository.owner.url);
    }

    private void bindRepositoryData(Repository repository) {
        setTitle(repository.name);
        textDescription.setText(repository.description);
        textHomePage.setText(repository.homepage);
        textHomePage.setVisibility(repository.hasHomepage() ? View.VISIBLE : View.GONE);
        textLanguage.setText(getString(R.string.text_language, repository.language));
        textLanguage.setVisibility(repository.hasLanguage() ? View.VISIBLE : View.GONE);
        textFork.setVisibility(repository.isFork() ? View.VISIBLE : View.GONE);
        Picasso.with(this)
                .load(repository.owner.avatarUrl)
                .placeholder(R.drawable.placeholder)
                .into(imgCircle);

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showOwner(User owner) {
        textOwner.setText(owner.name);
        textEmail.setText(owner.email);
        textEmail.setVisibility(owner.hasEmail() ? View.VISIBLE : View.GONE);
        textAddress.setText(owner.location);
        textAddress.setVisibility(owner.hasLocation() ? View.VISIBLE : View.GONE);
    }
}
