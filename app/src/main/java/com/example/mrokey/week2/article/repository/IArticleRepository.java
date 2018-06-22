package com.example.mrokey.week2.article.repository;

public interface IArticleRepository {
    void getListArticle(DataListener dataListener, int page);

    void getArticle(DataListener dataListener);
}
