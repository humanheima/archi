package com.brother.dmwmvvm.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.brother.dmwmvvm.R;
import com.brother.dmwmvvm.databinding.ActivityRepositoryBinding;
import com.brother.dmwmvvm.model.Repository;
import com.brother.dmwmvvm.viewmodel.RepositoryViewModel;

public class RepositoryActivity extends AppCompatActivity {

    public static final String REPOSITORY = "REPOSITORY";

    private ActivityRepositoryBinding binding;
    private RepositoryViewModel model;

    public static void launch(Context context, Repository repository) {
        Intent starter = new Intent(context, RepositoryActivity.class);
        starter.putExtra(REPOSITORY, repository);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repository);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Repository repository = getIntent().getParcelableExtra(REPOSITORY);
        model = new RepositoryViewModel(this, repository);
        binding.setViewModel(model);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.destroy();
    }
}
