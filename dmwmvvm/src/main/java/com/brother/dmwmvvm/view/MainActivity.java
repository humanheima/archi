package com.brother.dmwmvvm.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;

import com.brother.dmwmvvm.R;
import com.brother.dmwmvvm.adapter.RepositoryAdapter;
import com.brother.dmwmvvm.databinding.ActivityMainBinding;
import com.brother.dmwmvvm.model.Repository;
import com.brother.dmwmvvm.viewmodel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainViewModel.DataListener {

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new MainViewModel(this, this);
        binding.setViewModel(mainViewModel);
        setSupportActionBar(binding.toolbar);
        setupRecyclerView(binding.rv);
    }

    @Override
    public void onRepositoriesChanged(List<Repository> repositories) {
        RepositoryAdapter adapter =
                (RepositoryAdapter) binding.rv.getAdapter();
        adapter.setRepositories(repositories);
        adapter.notifyDataSetChanged();
        hideSoftKeyboard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainViewModel.destroy();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RepositoryAdapter adapter = new RepositoryAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.editTextUsername.getWindowToken(), 0);
    }
}
