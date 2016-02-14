package com.example.jiaweishi.articlesearch.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.jiaweishi.articlesearch.R;
import com.example.jiaweishi.articlesearch.models.Article;

public class ArticleDetailActivity extends AppCompatActivity {
    private WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        mWebview = (WebView) findViewById(R.id.webview);
        // Configure related browser settings
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        mWebview.setWebViewClient(new MyBrowser());

        Article article = (Article) getIntent().getSerializableExtra("article");
        mWebview.loadUrl(article.getWebUrl());
    }


    // Manages the behavior when URLs are loaded
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

