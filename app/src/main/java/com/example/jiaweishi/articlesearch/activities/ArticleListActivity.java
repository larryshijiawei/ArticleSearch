package com.example.jiaweishi.articlesearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.jiaweishi.articlesearch.R;
import com.example.jiaweishi.articlesearch.adapters.ArticleAdapter;
import com.example.jiaweishi.articlesearch.fragment.DatePickerFragment;
import com.example.jiaweishi.articlesearch.fragment.FilterFragment;
import com.example.jiaweishi.articlesearch.fragment.FilterFragmentCallback;
import com.example.jiaweishi.articlesearch.models.Article;
import com.example.jiaweishi.articlesearch.models.EndlessRecyclerViewScrollListener;
import com.example.jiaweishi.articlesearch.models.Filter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ArticleListActivity extends AppCompatActivity implements FilterFragmentCallback,
        DatePickerDialog.OnDateSetListener{
    private final String TAG = ArticleListActivity.class.getSimpleName();

    private final String baseUrl = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private final String apiKey = "7837482c431734422e608fe4de337f99:2:74353198";

    private List<Article> mArticleList = new ArrayList<>();
    private Filter mFilter = new Filter();
    private String mKeyword = null;
    private ArticleAdapter mAdapter;
    private boolean dateInfoReceived = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        initAdapter();

    }

    private void initAdapter(){
        RecyclerView rvArticles = (RecyclerView) findViewById(R.id.rvArticles);

        mArticleList = new ArrayList<>();
        mAdapter = new ArticleAdapter(getApplicationContext(), mArticleList);
        rvArticles.setAdapter(mAdapter);

        //listener which triggers the webview
        mAdapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.d(TAG, "========= on click item " + position);
                Article selectedArticle = mArticleList.get(position);
                Intent intent = new Intent(getApplicationContext(), ArticleDetailActivity.class);
                intent.putExtra("article", selectedArticle);
                startActivity(intent);
            }
        });

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvArticles.setLayoutManager(gridLayoutManager);

        //on scroll listener--support endless scroll
        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG, " ========= scroll on page " + page);
                fetchArticles(mKeyword, page);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article_list, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                initAdapter();
                fetchArticles(query, 0);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Activate once the "settings" option is selected
        if(item.getItemId() == R.id.action_settings){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FilterFragment filterDialog = FilterFragment.getInstance(this);
            filterDialog.show(fragmentManager, "filter");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFilterSaved(Filter filter) {
        Toast.makeText(getApplicationContext(), "Save Filter", Toast.LENGTH_LONG).show();
        mFilter = filter;
    }

    @Override
    public void onDatePickerTriggered() {
        dateInfoReceived = false;

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        if(dateInfoReceived)
            return;

        dateInfoReceived = true;
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FilterFragment filterDialog = FilterFragment.getInstance(this);
        filterDialog.setDateInfo(c.getTime());
        filterDialog.show(fragmentManager, "filter");

    }

    private void fetchArticles(String keyword, int page){
        mKeyword = keyword;

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = getRequestParam(page);

        client.get(baseUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    try {
                        parseArticleJson(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.e(TAG, "Error in fetch article, status code " + statusCode);
            }
        });
    }



    private void parseArticleJson(JSONObject json) throws JSONException {
        //mArticleList.clear();

        JSONObject response = json.getJSONObject("response");
        JSONArray docs = response.getJSONArray("docs");
        for(int i=0; i<docs.length(); i++){
            Article article = new Article(docs.getJSONObject(i));
            mArticleList.add(article);
        }
        mAdapter.notifyDataSetChanged();
    }

    private RequestParams getRequestParam(int page){
        RequestParams requestParams = new RequestParams();
        if(mKeyword != null)
            requestParams.add("q", mKeyword);
        requestParams.add("page", Integer.toString(page));
        requestParams.add("sort", mFilter.getSortOrder());
        if(mFilter.getBeginDate() != null){
            String dateInfo = new SimpleDateFormat("yyyyMMdd").format(mFilter.getBeginDate());
            requestParams.add("begin_date", dateInfo);
        }
        requestParams.add("api-key", apiKey);

        String str = "";
        for(String category : mFilter.getCategories()){
            str += " \"" + category +"\"";
        }
        //remove the first space
        str.trim();

        if(str.length() > 0){
            str = "news_desk:(" + str + ")";
            requestParams.put("fq", str);
        }

        return requestParams;
    }

}
