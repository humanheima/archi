package com.brotherd.dmwappmvp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brotherd.dmwappmvp.R;
import com.brotherd.dmwappmvp.adapter.RepositoryAdapter;
import com.brotherd.dmwappmvp.adapter.TextWatcherAdapter;
import com.brotherd.dmwappmvp.model.Repository;
import com.brotherd.dmwappmvp.presenter.MainPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainMvpView {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.editUserName)
    EditText editUserName;
    @BindView(R.id.btn_search)
    ImageButton btnSearch;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.text_info)
    TextView textInfo;
    @BindView(R.id.rv_repository)
    RecyclerView rvRepository;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter();
        presenter.attachView(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupRecyclerView(rvRepository);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editUserName.getText().toString();
                if (!TextUtils.isEmpty(userName)) {
                    presenter.loadRepositories(userName);
                }
            }
        });
        editUserName.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSearch.setVisibility(s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
        editUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String userName = editUserName.getText().toString();
                    if (!TextUtils.isEmpty(userName)) {
                        presenter.loadRepositories(userName);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private void setupRecyclerView(RecyclerView rvRepository) {
        RepositoryAdapter adapter = new RepositoryAdapter();
        adapter.setCallback(new RepositoryAdapter.Callback() {
            @Override
            public void onItemClick(Repository repository) {
                RepositoryActivity.launch(MainActivity.this, repository);
            }
        });
        rvRepository.setAdapter(adapter);
        rvRepository.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showRepositories(List<Repository> repositories) {
        RepositoryAdapter adapter = (RepositoryAdapter) rvRepository.getAdapter();
        adapter.setRepositories(repositories);
        adapter.notifyDataSetChanged();
        rvRepository.requestFocus();
        hideSoftKeyboard();
        progressBar.setVisibility(View.INVISIBLE);
        textInfo.setVisibility(View.INVISIBLE);
        rvRepository.setVisibility(View.VISIBLE);
    }


    @Override
    public void showMessage(int stringId) {
        progressBar.setVisibility(View.INVISIBLE);
        textInfo.setVisibility(View.VISIBLE);
        rvRepository.setVisibility(View.INVISIBLE);
        textInfo.setText(getString(stringId));
    }

    @Override
    public void showProgressIndicator() {
        progressBar.setVisibility(View.VISIBLE);
        textInfo.setVisibility(View.INVISIBLE);
        rvRepository.setVisibility(View.INVISIBLE);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editUserName.getWindowToken(), 0);
    }
}
