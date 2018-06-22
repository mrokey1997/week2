package com.example.mrokey.week2.article.presenter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

import com.example.mrokey.week2.R;
import com.example.mrokey.week2.article.repository.DataListener;
import com.example.mrokey.week2.article.repository.IArticleRepository;
import com.example.mrokey.week2.article.view.IListArticleActivity;
import com.example.mrokey.week2.model.Doc;

import java.util.List;

public class ListArticlePresenter implements IListArticlePresenter, DataListener {
    private IListArticleActivity mView;
    private IArticleRepository articleRepository;
    private Context context;

    public ListArticlePresenter(Context context, IListArticleActivity mView, IArticleRepository articleRepository) {
        this.context = context;
        this.mView = mView;
        this.articleRepository = articleRepository;
    }

    @Override
    public void getListArticle(int page) {
        mView.showLoading();
        articleRepository.getListArticle(this, page);
    }

    @Override
    public void getArticle() {
        articleRepository.getArticle(this);
    }

    /**
     * Put search query into local data and call API with search query
     * @param query ...
     */
    @Override
    public void onQueryTextSubmit(String query) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saved_data", Context.MODE_PRIVATE).edit();
        editor.putString("query", query);
        editor.apply();
        getListArticle(1);
    }

    /**
     * Put search query (when change) into local data and call API with search query
     * @param newText ...
     */
    @Override
    public void onQueryTextChange(String newText) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saved_data", Context.MODE_PRIVATE).edit();
        editor.putString("query", newText);
        editor.apply();
        getListArticle(1);
    }

    /**
     * Clear query search history in local data
     */
    @Override
    public void clearSearchQueryInLocalData() {
        SharedPreferences.Editor editor = context.getSharedPreferences("saved_data", Context.MODE_PRIVATE).edit();
        editor.putString("query", "");
        editor.apply();
    }

    /**
     * Check filter is just work
     * @return true or false
     */
    @Override
    public boolean isJustFilter() {
        SharedPreferences reader = context.getSharedPreferences("saved_data", Context.MODE_PRIVATE);
        return reader.getString("just_filter", "false").equals("true");
    }

    /**
     * Set "false" for "just_filer" in local data
     */
    @Override
    public void setJustFilterFalse() {
        SharedPreferences.Editor editor = context.getSharedPreferences("saved_data", Context.MODE_PRIVATE).edit();
        editor.putString("just_filter", "false");
        editor.apply();
    }

    /**
     * Show list of article when call API success
     * @param docs list of article
     */
    @Override
    public void onResponse(List<Doc> docs) {
        mView.hideLoading();
        mView.setDataArticleAdapter(docs);
    }

    /**
     * Handle event when call API failed
     * @param error ...
     */
    @Override
    public void onFailure(String error) {
        mView.hideLoading();
        mView.showNotifyError();
    }

    /**
     * show detail article when click on an item of RecyclerView
     * @param customTabsIntent custom tab
     * @param url url of article
     */
    @Override
    public void onClickItem(CustomTabsIntent customTabsIntent, String url) {
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
