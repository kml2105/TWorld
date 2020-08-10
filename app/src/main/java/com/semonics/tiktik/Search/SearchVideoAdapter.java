package com.semonics.tiktik.Search;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.semonics.tiktik.Home.HomeModel;
import com.semonics.tiktik.R;
import com.semonics.tiktik.SimpleClasses.Variables;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class SearchVideoAdapter extends RecyclerView.Adapter<SearchVideoAdapter.CustomViewHolder > implements Filterable {
    public Context context;

    ArrayList<HomeModel> datalist;
    ArrayList<HomeModel> datalist_filter;

    public interface OnItemClickListener {
        void onItemClick(ArrayList<HomeModel> video_list, int postion);
    }

    public SearchVideoAdapter.OnItemClickListener listener;

    public SearchVideoAdapter(Context context, ArrayList<HomeModel> arrayList, SearchVideoAdapter.OnItemClickListener listener) {
        this.context = context;
        datalist = arrayList;
        datalist_filter=arrayList;
        this.listener = listener;
    }


    @Override
    public SearchVideoAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_video, viewGroup, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        SearchVideoAdapter.CustomViewHolder viewHolder = new SearchVideoAdapter.CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView video_thumbnail;


        public CustomViewHolder(View view) {
            super(view);
            video_thumbnail = view.findViewById(R.id.video_thumbnail);

        }

        public void bind(final int pos, final ArrayList<HomeModel> datalist) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(datalist,pos);
                }
            });
        }


    }


    @Override
    public void onBindViewHolder(final SearchVideoAdapter.CustomViewHolder holder, final int i) {
//        datalist_filter.clear();
        HomeModel item = datalist.get(i);
        holder.bind(i, datalist);
        Picasso.with(context).load(item.thumb).resize(100, 100).centerCrop().into(holder.video_thumbnail);
    }


    // that function will filter the result
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    datalist_filter = datalist;
                } else {
                    ArrayList<HomeModel> filteredList = new ArrayList<>();
                    for (HomeModel row : datalist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.caption.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    datalist_filter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = datalist_filter;
                return filterResults;

            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                datalist_filter = (ArrayList<HomeModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}