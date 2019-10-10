package com.example.myapplication.Adapters;

import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Filter;
import java.util.ArrayList;
import java.util.List;


public class AutoSuggestAdapter extends ArrayAdapter<String> implements Filterable {
    private List<String> mlistNewsData;
    public AutoSuggestAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mlistNewsData = new ArrayList<>();
    }
    public void setNewsData(List<String> list) {
        mlistNewsData.clear();
        mlistNewsData.addAll(list);
    }
    @Override
    public int getCount() {
        return mlistNewsData.size();
    }
    @Nullable
    @Override
    public String getItem(int position) {
        return mlistNewsData.get(position);
    }
    /**
     * Used to Return the full object directly from adapter.
     *
     * @param position
     * @return
     */
    public String getObject(int position) {
        return mlistNewsData.get(position);
    }
    @NonNull
    @Override
    public Filter getFilter() {
        Filter NewsDataFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = mlistNewsData;
                    filterResults.count = mlistNewsData.size();
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) {
                    notifyNewsDataSetChanged();
                } else {
                    notifyNewsDataSetInvalidated();
                }
            }
        };
        return NewsDataFilter;
    }

}
