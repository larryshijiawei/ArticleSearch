package com.example.jiaweishi.articlesearch.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

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

                fetchArticles(query);

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

    private void fetchArticles(String keyword){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = getRequestParam(keyword);

        client.get(baseUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    try {
                        parseArticleJson(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    displayArticles();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.e(TAG, "Error in fetch article, status code " + statusCode);
            }
        });
    }

    private void parseArticleJson(JSONObject json) throws JSONException {
        mArticleList.clear();

        JSONObject response = json.getJSONObject("response");
        JSONArray docs = response.getJSONArray("docs");
        for(int i=0; i<docs.length(); i++){
            Article article = new Article(docs.getJSONObject(i));
            mArticleList.add(article);
        }
    }

    private void displayArticles(){
        GridView gridView = (GridView) findViewById(R.id.gv_articleList);

        ArticleAdapter adapter = new ArticleAdapter(this.getApplicationContext(), mArticleList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article selectedArticle = (Article)adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getApplicationContext(), ArticleDetailActivity.class);
                intent.putExtra("article", selectedArticle);
                startActivity(intent);

            }
        });
    }

    // Manages the behavior when URLs are loaded
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private RequestParams getRequestParam(String keyword){
        RequestParams requestParams = new RequestParams();
        requestParams.add("q", keyword);
        requestParams.add("page", Integer.toString(currentPage));
        requestParams.add("sort", mFilter.getSortOrder());
        if(mFilter.getBeginDate() != null){
            String dateInfo = new SimpleDateFormat("yyyyMMdd").format(mFilter.getBeginDate());
            requestParams.add("begin_date", dateInfo);
        }
        requestParams.add("api-key", apiKey);


        return requestParams;
    }

}
