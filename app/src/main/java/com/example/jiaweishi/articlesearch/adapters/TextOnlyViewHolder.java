package com.example.jiaweishi.articlesearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jiaweishi.articlesearch.R;

/**
 * Created by jiaweishi on 2/14/16.
 */
public class TextOnlyViewHolder extends RecyclerView.ViewHolder  {

    private TextView textView;

    private ComplexRecycleViewAdapter mAdapter;

    public TextOnlyViewHolder(final View itemView, ComplexRecycleViewAdapter adapter) {
        super(itemView);

        mAdapter = adapter;

        textView = (TextView) itemView.findViewById(R.id.tv_text1);

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

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
