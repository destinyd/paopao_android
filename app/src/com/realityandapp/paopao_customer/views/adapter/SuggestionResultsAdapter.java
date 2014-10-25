package com.realityandapp.paopao_customer.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.realityandapp.paopao_customer.R;

import java.util.ArrayList;

/**
 * Created by dd on 14-9-18.
 */
public class SuggestionResultsAdapter extends ArrayAdapter<SuggestionResult.SuggestionInfo> {
    private ArrayList<SuggestionResult.SuggestionInfo> items;
    private ArrayList<SuggestionResult.SuggestionInfo> itemsAll;
    private ArrayList<SuggestionResult.SuggestionInfo> suggestions;
    private int viewResourceId;

    public SuggestionResultsAdapter(Context context, int viewResourceId, ArrayList<SuggestionResult.SuggestionInfo> items) {
        super(context, viewResourceId);
        this.items = items;
        this.itemsAll = (ArrayList<SuggestionResult.SuggestionInfo>) items.clone();
        this.suggestions = new ArrayList<SuggestionResult.SuggestionInfo>();
        this.viewResourceId = viewResourceId;
    }

    public SuggestionResultsAdapter(Context context, int viewResourceId) {
        super(context, viewResourceId);
        this.items = new ArrayList<SuggestionResult.SuggestionInfo>();
        this.itemsAll = (ArrayList<SuggestionResult.SuggestionInfo>) items.clone();
        this.suggestions = new ArrayList<SuggestionResult.SuggestionInfo>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        SuggestionResult.SuggestionInfo suggestion_info = items.get(position);
        if (suggestion_info != null) {
            TextView tv_suggestion_info = (TextView) v.findViewById(R.id.tv_suggestion);
            if (tv_suggestion_info != null) {
                tv_suggestion_info.setText(suggestion_info.key);
            }
        }
        return v;
    }

    @Override
    public void add(SuggestionResult.SuggestionInfo object) {
        // todo maybe something bug in here
//        super.add(object);
        items.add(object);
        itemsAll.add(object);
    }

    @Override
    public void clear() {
        // todo something bug in here
//        super.clear();
        items.clear();
        itemsAll.clear();
        suggestions.clear();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    @Override
    public SuggestionResult.SuggestionInfo getItem(int position) {
        return items.get(position);
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((SuggestionResult.SuggestionInfo) (resultValue)).key;
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (SuggestionResult.SuggestionInfo suggestion_info : itemsAll) {
                    suggestions.add(suggestion_info);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<SuggestionResult.SuggestionInfo> filteredList = (ArrayList<SuggestionResult.SuggestionInfo>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (SuggestionResult.SuggestionInfo c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}