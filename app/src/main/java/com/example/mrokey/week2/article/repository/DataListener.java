package com.example.mrokey.week2.article.repository;

import android.support.customtabs.CustomTabsIntent;

import com.example.mrokey.week2.model.Doc;

import java.util.List;

public interface DataListener {
    void onResponse(List<Doc> docs);

    void onFailure(String error);

    void onClickItem(CustomTabsIntent customTabsIntent, String url);

    void onTryAngain();
}
