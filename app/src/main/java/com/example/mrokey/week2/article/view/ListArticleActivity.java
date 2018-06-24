package com.example.mrokey.week2.article.view;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mrokey.week2.R;
import com.example.mrokey.week2.adapter.ArticleAdapter;
import com.example.mrokey.week2.filter.view.FilterActivity;
import com.example.mrokey.week2.article.presenter.IListArticlePresenter;
import com.example.mrokey.week2.article.presenter.ListArticlePresenter;
import com.example.mrokey.week2.article.repository.ArticleRepository;
import com.example.mrokey.week2.article.repository.IArticleRepository;
import com.example.mrokey.week2.fragment.NoInternetConnectionDialog;
import com.example.mrokey.week2.model.Doc;
import com.example.mrokey.week2.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListArticleActivity extends AppCompatActivity implements IListArticleActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view_article)
    RecyclerView recycler_view;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;

    ArticleAdapter articleAdapter;

    IListArticlePresenter listArticlePresenter;

    EndlessRecyclerViewScrollListener scrollListener;

    List<Doc> list_doc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        list_doc = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this);
        initRecyclerView();

        initSwipeRefreshLayout();

        IArticleRepository articleRepository = new ArticleRepository(this, articleAdapter);
        listArticlePresenter = new ListArticlePresenter(this,this, articleRepository);

        if (articleAdapter.getItemCount() == 0)
            listArticlePresenter.getListArticle(1);
        else articleAdapter.setData(list_doc);

        // Get detail article in WebView
        listArticlePresenter.getArticle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* If just use filter then API will be call again*/
        if (listArticlePresenter.isJustFilter()) {
            articleAdapter.clearData();
            scrollListener.resetState();
            listArticlePresenter.getListArticle(1);
            listArticlePresenter.setJustFilterFalse();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*Clear search query in local data when close app*/
        listArticlePresenter.clearSearchQueryInLocalData();
    }

    /**
     * Initialize SwipeRefreshLayout
     */
    private void initSwipeRefreshLayout() {
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                articleAdapter.clearData();
                listArticlePresenter.getListArticle(1);
                swipe_refresh_layout.setRefreshing(false);
            }
        });
    }

    /**
     * Initialize RecyclerView
     */
    private void initRecyclerView() {
        recycler_view.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setAdapter(articleAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listArticlePresenter.getListArticle(page + 1);
            }
        };
        recycler_view.addOnScrollListener(scrollListener);
    }

    /**
     * Open filter when click filter item on toolbar
     * @param item item
     * @return ...
     */
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

    /**
     * Create items on toolbar
     * @param menu ...
     * @return ...
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        /* Search */
        MenuItem searchItem = menu.findItem(R.id.miSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                articleAdapter.clearData();
                scrollListener.resetState();
                listArticlePresenter.onQueryTextSubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                articleAdapter.clearData();
                scrollListener.resetState();
                listArticlePresenter.onQueryTextChange(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Display progressbar
     */
    @Override
    public void showLoading() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    /**
     * hide progressbar
     */
    @Override
    public void hideLoading() {
        if (progress_bar.isShown())
            progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void setDataArticleAdapter(List<Doc> docs) {
        list_doc = docs;
        articleAdapter.setData(list_doc);
    }

    /**
     * Display a error notification
     */
    @Override
    public void showNotifyError() {
        Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
    }
}
