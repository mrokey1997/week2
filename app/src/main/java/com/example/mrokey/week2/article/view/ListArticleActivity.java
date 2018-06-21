package com.example.mrokey.week2.article.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mrokey.week2.R;
import com.example.mrokey.week2.adapter.ArticleAdapter;
import com.example.mrokey.week2.adapter.ItemClickListener;
import com.example.mrokey.week2.article.FilterActivity;
import com.example.mrokey.week2.article.presenter.IListArticlePresenter;
import com.example.mrokey.week2.article.presenter.ListArticlePresenter;
import com.example.mrokey.week2.article.repository.ArticleRepository;
import com.example.mrokey.week2.article.repository.IArticleRepository;
import com.example.mrokey.week2.model.Doc;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListArticleActivity extends AppCompatActivity implements IListArticleActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //APILink apiLink;

    ArticleAdapter articleAdapter;

    IListArticlePresenter listArticlePresenter;

    @BindView(R.id.recycler_view_article)
    RecyclerView recycler_view;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //showDatePickerDialog();
        ButterKnife.bind(this);

        init();

        IArticleRepository articleRepository = new ArticleRepository(this);
        listArticlePresenter = new ListArticlePresenter(this, articleRepository);

        listArticlePresenter.getListArticle();

        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listArticlePresenter.getListArticle();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences("saved_data", MODE_PRIVATE).edit();
        editor.putString("query", "");
        editor.apply();
    }

    private void init() {
        initRecyclerView();

        initArticleAdapter();

        recycler_view.setAdapter(articleAdapter);
    }

    private void initArticleAdapter() {
        articleAdapter = new ArticleAdapter(this);
        articleAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClickItem(Doc doc) {

            }
        });
    }

    private void initRecyclerView() {
        recycler_view.hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miFilter: {
                Intent intent = new Intent(ListArticleActivity.this, FilterActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.miSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SharedPreferences.Editor editor = getSharedPreferences("saved_data", MODE_PRIVATE).edit();
                editor.putString("query", query);
                editor.apply();
                //getArticleSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SharedPreferences.Editor editor = getSharedPreferences("saved_data", MODE_PRIVATE).edit();
                editor.putString("query", newText);
                editor.apply();
                //getArticleSearch();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void showLoading() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (progress_bar.isShown())
            progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void showListArticle(List<Doc> docs) {
        articleAdapter.setData(docs);
    }

    @Override
    public void showNotifyError() {
        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
    }
}
