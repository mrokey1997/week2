package com.example.mrokey.week2.article.repository;

import com.example.mrokey.week2.model.Doc;

import java.util.List;

public interface DataListener {
    void onResponse(List<Doc> docs);

    void onFailure(String error);
}
