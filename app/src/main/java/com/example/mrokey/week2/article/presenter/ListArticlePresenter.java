package com.example.mrokey.week2.article.presenter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
    public void getListArticle() {
        mView.showLoading();
        articleRepository.getArticleSearch(this);
    }

    @Override
    public void getArticle(Doc doc) {
        String url = doc.getWebUrl();

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_share);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);

        int requestCode = 100;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setActionButton(bitmap, "Share this article", pendingIntent, true);
        CustomTabsIntent customTabsIntent = builder.build();

        customTabsIntent.launchUrl(context, Uri.parse(url));
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
