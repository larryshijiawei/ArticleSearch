package com.example.jiaweishi.articlesearch.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;


import com.example.jiaweishi.articlesearch.R;
import com.example.jiaweishi.articlesearch.models.Filter;

import org.w3c.dom.Text;


/**
 * Created by jiaweishi on 2/12/16.
 */
public class FilterFragment extends DialogFragment {
    private Context mContext;

    final int[] category_ids = {R.id.cb_filter_category_arts,
            R.id.cb_filter_category_fashion,
            R.id.cb_filter_category_sports};

    final String[] categories = {"Arts", "Fashion Style", "Sports"};

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        final View dialogView = view;

        Button bt_SaveFilter = (Button) view.findViewById(R.id.btn_saveFilter);
        bt_SaveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filter filter = fetchFilter(dialogView);
                ((FilterFragmentCallback) mContext).onFilterSaved(filter);
                dismiss();
            }
        });

        //spinner
        Spinner spinner = (Spinner) view.findViewById(R.id.sp_filter_sortOrder);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sortOrders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return super.onCreateDialog(savedInstanceState);
    }

    private Filter fetchFilter(View view){

        Filter filter = new Filter();
        EditText et_startDate = (EditText) view.findViewById(R.id.et_filter_beginDate);

        filter.setBeginDate(et_startDate.getText().toString());

        Spinner spinner = (Spinner) view.findViewById(R.id.sp_filter_sortOrder);
        filter.setSortOrder(spinner.getSelectedItem().toString());

        for(int i=0; i<category_ids.length; i++){
            CheckBox checkBox = (CheckBox) view.findViewById(category_ids[i]);
            if(checkBox.isChecked())
                filter.addCategory(categories[i]);
        }

        return filter;
    }
}
