package com.example.mrokey.week2.article;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //showDatePickerDialog();
        ButterKnife.bind(this);

        setupToolbar();

        getArticleSearch();
        
        init();
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

    public void getArticleSearch() {
        apiLink = APIRetrofit.createService();
        apiLink.getArticleSearch("20160112", "oldest", "news_desk:(\"Education\"%20\"Health\")").enqueue(new Callback<Artical>() {
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
