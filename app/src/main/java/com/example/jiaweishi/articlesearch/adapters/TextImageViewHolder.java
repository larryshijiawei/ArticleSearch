package com.example.jiaweishi.articlesearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jiaweishi.articlesearch.R;

/**
 * Created by jiaweishi on 2/14/16.
 */
public class TextImageViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public ImageView imageView;

    private ComplexRecycleViewAdapter mAdapter;

    public TextImageViewHolder(final View itemView, ComplexRecycleViewAdapter adapter) {
        super(itemView);
        mAdapter = adapter;

        textView = (TextView) itemView.findViewById(R.id.tv_text2);
        imageView = (ImageView) itemView.findViewById(R.id.iv_image2);

        // Setup the click listener
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                if (mAdapter.getOnItemClickListener() != null)
                    mAdapter.getOnItemClickListener().onItemClick(itemView, getLayoutPosition());
            }
        });
    }

    public TextView getTextView() {
        return textView;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
