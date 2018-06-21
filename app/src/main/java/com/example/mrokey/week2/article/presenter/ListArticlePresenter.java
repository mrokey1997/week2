package com.example.mrokey.week2.article.presenter;

import com.example.mrokey.week2.article.repository.DataListener;
import com.example.mrokey.week2.article.repository.IArticleRepository;
import com.example.mrokey.week2.article.view.IListArticleActivity;
import com.example.mrokey.week2.model.Doc;

import java.util.List;

public class ListArticlePresenter implements IListArticlePresenter, DataListener {
    private IListArticleActivity mView;
    private IArticleRepository articleRepository;

    public ListArticlePresenter(IListArticleActivity mView, IArticleRepository articleRepository) {
        this.mView = mView;
        this.articleRepository = articleRepository;
    }

    @Override
    public void getListArticle() {
        mView.showLoading();
        articleRepository.getArticleSearch(this);
    }

    @Override
    public void onResponse(List<Doc> docs) {
        mView.hideLoading();
        mView.showListArticle(docs);
    }

    @Override
    public void onFailure(String error) {
        mView.hideLoading();
        mView.showNotifyError();
    }


}
