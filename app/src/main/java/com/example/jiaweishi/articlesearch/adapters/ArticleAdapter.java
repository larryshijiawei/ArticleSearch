package com.example.jiaweishi.articlesearch.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jiaweishi.articlesearch.R;
import com.example.jiaweishi.articlesearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jiaweishi on 2/11/16.
 */
public class ArticleAdapter extends BaseAdapter{
    private Context mContext;
    private List<Article> mArticles;

    public ArticleAdapter(Context context, List<Article> list){
        mContext = context;
        mArticles = list;
    }

    @Override
    public int getCount() {
        return mArticles.size();
    }

    @Override
    public Object getItem(int i) {
        return mArticles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if(convertView == null){
            gridView = inflater.inflate(R.layout.article, null);

            Article article = mArticles.get(position);

            //set values
            TextView textView = (TextView) gridView.findViewById(R.id.tv_grid);
            textView.setText(article.getHeadline());

            ImageView imageView = (ImageView) gridView.findViewById(R.id.iv_grid);

            if(article.getImageLink() != null)
                Picasso.with(mContext).load(Uri.parse(article.getImageLink())).fit().into(imageView);

        }
        else
            gridView = convertView;


        return gridView;
    }
}
