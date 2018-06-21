package com.example.mrokey.week2.article.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mrokey.week2.api.APILink;
import com.example.mrokey.week2.api.APIRetrofit;
import com.example.mrokey.week2.model.Artical;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ArticleRepository implements IArticleRepository {
    private APILink apiLink;
    private Context context;

    public ArticleRepository(Context context) {
        this.context = context;
        apiLink = APIRetrofit.createService();
    }


    @Override
    public void getArticleSearch(final DataListener dataListener) {
        getCall().enqueue(new Callback<Artical>() {
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
     * Save state of filter
     * @return function call API
     */
    private Call<Artical> getCall() {
        String ARTS = "\"Arts\"";
        String FASHION_AND_STYLE = "\"Fashion & Style\"";
        String SPORTS = "\"Sports\"";

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
        boolean isQuery = false;
        if (!query.equals("")) {
            isQuery = true;
            isFQ = true;
        }

        if (isCheckbox_arts) fq += ARTS;
        if (isCheckbox_fashion_style) fq += FASHION_AND_STYLE;
        if (isCheckbox_sports) fq += SPORTS;
        if (isQuery) fq += query;
        fq = "news_desk:(" + fq + ")";

        if (isBegin_date && isFQ)
            return apiLink.getArticleSearch(begin_date, sort, fq);
        else if (isBegin_date && !isFQ)
            return apiLink.getArticleByBeginDateAndSort(begin_date, sort);
        else if (!isBegin_date && isFQ)
            return apiLink.getArticleBySortAndFQ(sort, fq);
        else return apiLink.getArticleBySort(sort);
    }
}
