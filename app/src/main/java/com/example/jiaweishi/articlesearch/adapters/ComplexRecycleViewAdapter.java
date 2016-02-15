package com.example.jiaweishi.articlesearch.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.jiaweishi.articlesearch.R;
import com.example.jiaweishi.articlesearch.models.Article;

import java.util.List;

/**
 * Created by jiaweishi on 2/14/16.
 */
public class ComplexRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /***** Creating OnItemClickListener *****/

    // Define listener member variable
    private static OnItemClickListener mListener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public OnItemClickListener getOnItemClickListener(){
        return mListener;
    }



    private List<Article> mArticles;
    private Context mContext;

    private final int TYPE_TEXTONLY = 0;
    private final int TYPE_TEXTIMAGE = 1;

    public ComplexRecycleViewAdapter(Context context, List<Article> articles) {
        this.mArticles = articles;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(viewType == TYPE_TEXTIMAGE){
            final View textImageView = inflater.inflate(R.layout.layout_viewholder_textimage, parent, false);
            viewHolder = new TextImageViewHolder(textImageView, this);
        }
        else{
            final View textOnlyView = inflater.inflate(R.layout.layout_viewholder_textonly, parent, false);
            viewHolder = new TextOnlyViewHolder(textOnlyView, this);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = mArticles.get(position);

        if(getItemViewType(position) == TYPE_TEXTIMAGE){
            TextImageViewHolder viewHolder = (TextImageViewHolder) holder;
            viewHolder.getTextView().setText(article.getHeadline());

            Glide.with(mContext).load(Uri.parse(article.getImageLink())).into(viewHolder.getImageView());
        }
        else{
            TextOnlyViewHolder viewHolder = (TextOnlyViewHolder) holder;
            viewHolder.getTextView().setText(article.getHeadline());
        }

    }

    @Override
    public int getItemViewType(int position) {
        Article article = mArticles.get(position);

        if(article.hasImageView())
            return TYPE_TEXTIMAGE;
        else
            return TYPE_TEXTONLY;
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
