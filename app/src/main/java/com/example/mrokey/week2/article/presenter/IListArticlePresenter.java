package com.example.mrokey.week2.article.presenter;

public interface IListArticlePresenter {
    void getListArticle(int page);

    void getArticle();

    void onQueryTextSubmit(String query);

    void onQueryTextChange(String newText);

    void clearSearchQueryInLocalData();

    boolean isJustFilter();

    void setJustFilterFalse();
}
