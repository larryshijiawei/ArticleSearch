package com.example.jiaweishi.articlesearch.fragment;

import com.example.jiaweishi.articlesearch.models.Filter;

/**
 * Created by jiaweishi on 2/13/16.
 */
public interface FilterFragmentCallback {
    public void onFilterSaved(Filter filter);

    public void onDatePickerTriggered();
}
