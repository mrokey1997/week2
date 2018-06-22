package com.example.mrokey.week2.article.view;

import com.example.mrokey.week2.model.Doc;

import java.util.List;

public interface IListArticleActivity {
    void showLoading();

    void hideLoading();

    void setDataArticleAdapter(List<Doc> docs);

    void showNotifyError();
}
