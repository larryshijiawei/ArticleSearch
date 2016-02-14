package com.example.jiaweishi.articlesearch.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.jiaweishi.articlesearch.R;
import com.example.jiaweishi.articlesearch.adapters.ArticleAdapter;
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
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ArticleListActivity extends AppCompatActivity implements FilterFragmentCallback{
    private final String TAG = ArticleListActivity.class.getSimpleName();

    //http://api.nytimes.com/svc/search/v2/articlesearch.json?q=new+york
    // &fq=news_desk:("Sports" "Arts" "Fashion Style")
    // &begin_date=20160116&end_date=20160201
    // &api-key=7837482c431734422e608fe4de337f99:2:74353198

    private final String baseUrl = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private final String apiKey = "7837482c431734422e608fe4de337f99:2:74353198";

    private List<Article> mArticleList = new ArrayList<>();
    private Filter mFilter = new Filter();
    private String mKeyword = null;
    private ArticleAdapter mAdapter;

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


        return requestParams;
    }

}
