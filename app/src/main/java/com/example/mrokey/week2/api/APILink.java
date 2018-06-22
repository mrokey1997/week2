package com.example.mrokey.week2.api;

import com.example.mrokey.week2.model.Artical;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APILink {
    @GET("articlesearch.json")
    Call<Artical> getArticleBySort(@Query("sort") String sort, @Query("q") String query, @Query("page") int page);

    @GET("articlesearch.json")
    Call<Artical> getArticleByBeginDate(@Query("sort") String sort, @Query("begin_date") String begin_date, @Query("q") String query, @Query("page") int page);

    @GET("articlesearch.json")
    Call<Artical> getArticleByFQ(@Query("sort") String sort, @Query("fq") String fq, @Query("q") String query, @Query("page") int page);

    @GET("articlesearch.json")
    Call<Artical> getArticleByBeginDateAndFQ(@Query("sort") String sort, @Query("begin_date") String begin_date, @Query("fq") String fq, @Query("q") String query, @Query("page") int page);
}
