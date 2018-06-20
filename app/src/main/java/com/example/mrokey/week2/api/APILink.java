package com.example.mrokey.week2.api;

import com.example.mrokey.week2.model.Artical;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APILink {
    @GET("articlesearch.json")
    Call<Artical> getArticleByBeginDate(@Query("begin_date") String begin_date);

    @GET("articlesearch.json")
    Call<Artical> getArticleBySort(@Query("sort") String sort);

    @GET("articlesearch.json")
    Call<Artical> getArticleByFQ(@Query("fq") String fq);

    @GET("articlesearch.json")
    Call<Artical> getArticleByBeginDateAndSort(@Query("begin_date") String begin_date, @Query("sort") String sort);

    @GET("articlesearch.json")
    Call<Artical> getArticleBySortAndFQ(@Query("sort") String sort, @Query("fq") String fq);

    @GET("articlesearch.json")
    Call<Artical> getArticleSearch(@Query("begin_date") String begin_date, @Query("sort") String sort, @Query("fq") String fq);
}
