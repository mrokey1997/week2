package com.example.mrokey.week2.article;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mrokey.week2.R;
import com.example.mrokey.week2.adapter.ArticleAdapter;
import com.example.mrokey.week2.adapter.ItemClickListener;
import com.example.mrokey.week2.api.APILink;
import com.example.mrokey.week2.api.APIRetrofit;
import com.example.mrokey.week2.model.Artical;
import com.example.mrokey.week2.model.Doc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    APILink apiLink;

    ArticleAdapter articleAdapter;

    List<Doc> list_docs;

    @BindView(R.id.recycler_view_article)
    RecyclerView recycler_view;

    final String ARTS = "\"Arts\"";
    final String FASHION_AND_STYLE = "\"Fashion & Style\"";
    final String SPORTS = "\"Sports\"";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //showDatePickerDialog();
        ButterKnife.bind(this);

        setupToolbar();
        
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getArticleSearch();
    }

    private void init() {
        list_docs = new ArrayList<>();

        initRecyclerView();

        initArticleAdapter();

        this.recycler_view.setAdapter(articleAdapter);
    }

    private void initArticleAdapter() {
        articleAdapter = new ArticleAdapter(MainActivity.this);
        articleAdapter.setData(list_docs);
        articleAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClickItem(Doc doc) {

            }
        });
    }

    private void initRecyclerView() {
        recycler_view.hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miFilter: {
                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public Call<Artical> getCall() {
        SharedPreferences reader = getSharedPreferences("saved_data", MODE_PRIVATE);

        String begin_date = reader.getString("begin_date", "fail");
        boolean isBegin_date = false;
        if (!begin_date.equals("fail"))
            isBegin_date = true;


        String sort = reader.getString("spinner", "newest");

        String fq = "";

        boolean isNoCheckbox = true;

        String checkbox_arts = reader.getString("checkbox_arts", "false");
        boolean isCheckbox_arts = false;
        if (!checkbox_arts.equals("false")) {
            isCheckbox_arts = true;
            isNoCheckbox = false;
        }

        String checkbox_fashion_style = reader.getString("checkbox_fashion_style", "false");
        boolean isCheckbox_fashion_style = false;
        if (!checkbox_fashion_style.equals("false")) {
            isCheckbox_fashion_style = true;
            isNoCheckbox = false;
        }

        String checkbox_sports = reader.getString("checkbox_sports", "false");
        boolean isCheckbox_sports = false;
        if (!checkbox_sports.equals("false")) {
            isCheckbox_sports = true;
            isNoCheckbox = false;
        }

        if (isCheckbox_arts) fq += ARTS;
        if (isCheckbox_fashion_style) fq += FASHION_AND_STYLE;
        if (isCheckbox_sports) fq += SPORTS;
        fq = "news_desk:(" + fq + ")";

        if (isBegin_date && !isNoCheckbox)
            return apiLink.getArticleSearch(begin_date, sort, fq);
        else if (isBegin_date && isNoCheckbox)
            return apiLink.getArticleByBeginDateAndSort(begin_date, sort);
        else if (!isBegin_date && !isNoCheckbox)
            return apiLink.getArticleBySortAndFQ(sort, fq);
        else return apiLink.getArticleBySort(sort);
    }



    public void getArticleSearch() {

        apiLink = APIRetrofit.createService();
        getCall().enqueue(new Callback<Artical>() {
            @Override
            public void onResponse(Call<Artical> call, Response<Artical> response) {
                if (response.body() != null) {
                    if (response.body().getResponse().getDocs() != null) {
                        articleAdapter.setData(response.body().getResponse().getDocs());
                        Log.d("Response", "Success");
                        Log.d("Response", response.body().getResponse().getDocs().size() + "");
                    }
                    else Log.d("Response", "Success but getDocs is null");
                }
            }

            @Override
            public void onFailure(Call<Artical> call, Throwable t) {

            }
        });
    }
}
