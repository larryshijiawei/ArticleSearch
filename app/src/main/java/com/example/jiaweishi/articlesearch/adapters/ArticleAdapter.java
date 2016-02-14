package com.example.jiaweishi.articlesearch.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{

    /***** Creating OnItemClickListener *****/

    // Define listener member variable
    private static OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewTitle;
        public ImageView imageViewIcon;


        public ViewHolder(final View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.tv_grid);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.iv_grid);

            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }

    }



    private List<Article> mArticles;
    private Context mContext;

    public ArticleAdapter(Context context, List<Article> list){
        mContext = context;
        mArticles = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.article, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = mArticles.get(position);

        TextView textView = holder.textViewTitle;
        textView.setText(article.getHeadline());

        ImageView imageView =  holder.imageViewIcon;
        if(article.getImageLink() != null)
            Picasso.with(mContext).load(Uri.parse(article.getImageLink())).fit().into(imageView);
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

}
