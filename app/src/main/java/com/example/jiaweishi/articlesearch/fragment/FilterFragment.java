package com.example.jiaweishi.articlesearch.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.example.jiaweishi.articlesearch.R;

/**
 * Created by jiaweishi on 2/12/16.
 */
public class FilterFragment extends DialogFragment {
    private Context mContext;

    public FilterFragment(){

    }

    private void setContext(Context context){
        mContext = context;
    }

    public static FilterFragment getInstance(Context context){
        FilterFragment fragment = new FilterFragment();
        fragment.setContext(context);
        Bundle arg = new Bundle();
//        arg.putString("title", title);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Button bt_SaveFilter = (Button) view.findViewById(R.id.btn_saveFilter);
        bt_SaveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FilterFragmentCallback) mContext).onSettingSaved();
                dismiss();
            }
        });

    }



}
