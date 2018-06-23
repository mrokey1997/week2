package com.example.mrokey.week2.article.repository;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;

import com.example.mrokey.week2.R;
import com.example.mrokey.week2.adapter.ArticleAdapter;
import com.example.mrokey.week2.adapter.ItemClickListener;
import com.example.mrokey.week2.api.APILink;
import com.example.mrokey.week2.api.APIRetrofit;
import com.example.mrokey.week2.model.Artical;
import com.example.mrokey.week2.model.Doc;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ArticleRepository implements IArticleRepository {
    private APILink apiLink;
    private Context context;
    private ArticleAdapter articleAdapter;

    public ArticleRepository(Context context, ArticleAdapter articleAdapter) {
        this.context = context;
        this.articleAdapter = articleAdapter;
        apiLink = APIRetrofit.createService();
    }

    /**
     * Initialize data when click on an item of RecyclerView
     * @param dataListener ...
     */
    @Override
    public void getArticle(final DataListener dataListener) {
        articleAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClickItem(Doc doc) {
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

                dataListener.onClickItem(customTabsIntent, url);
            }
        });
    }

    /**
     * Call API
     * @param dataListener ...
     * @param page ...
     */
    @Override
    public void getListArticle(final DataListener dataListener, int page) {
        getCall(page).enqueue(new Callback<Artical>() {
            @Override
            public void onResponse(Call<Artical> call, Response<Artical> response) {
                if (response.body() != null) {
                    if (response.body().getResponse().getDocs() != null) {
                        dataListener.onResponse(response.body().getResponse().getDocs());
                        Log.d("Response", response.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Artical> call, Throwable t) {
                dataListener.onFailure(t.getMessage());
            }
        });
    }


    /**
     * Read state of filter from local data
     * @return function call API
     */
    private Call<Artical> getCall(int page) {
        final String ARTS = "\"Arts\"";
        final String FASHION_AND_STYLE = "\"Fashion & Style\"";
        final String SPORTS = "\"Sports\"";

        SharedPreferences reader = context.getSharedPreferences("saved_data", MODE_PRIVATE);

        String begin_date = reader.getString("begin_date", "fail");
        boolean isBegin_date = false;
        if (!begin_date.equals("fail"))
            isBegin_date = true;

        String sort = reader.getString("spinner", "newest");

        String fq = "";

        boolean isFQ = false;

        String checkbox_arts = reader.getString("checkbox_arts", "false");
        boolean isCheckbox_arts = false;
        if (!checkbox_arts.equals("false")) {
            isCheckbox_arts = true;
            isFQ = true;
        }

        String checkbox_fashion_style = reader.getString("checkbox_fashion_style", "false");
        boolean isCheckbox_fashion_style = false;
        if (!checkbox_fashion_style.equals("false")) {
            isCheckbox_fashion_style = true;
            isFQ = true;
        }

        String checkbox_sports = reader.getString("checkbox_sports", "false");
        boolean isCheckbox_sports = false;
        if (!checkbox_sports.equals("false")) {
            isCheckbox_sports = true;
            isFQ = true;
        }

        String query = reader.getString("query", "");

        if (isCheckbox_arts) fq += ARTS;
        if (isCheckbox_fashion_style) fq += FASHION_AND_STYLE;
        if (isCheckbox_sports) fq += SPORTS;

        fq = "news_desk:(" + fq + ")";

        if (isBegin_date && isFQ)
            return apiLink.getArticleByBeginDateAndFQ(sort, begin_date, fq, query, page);
        else if (isBegin_date && !isFQ)
            return apiLink.getArticleByBeginDate(sort, begin_date, query, page);
        else if (!isBegin_date && isFQ)
            return apiLink.getArticleByFQ(sort, fq, query, page);
        else return apiLink.getArticleBySort(sort, query, page);
    }
}
