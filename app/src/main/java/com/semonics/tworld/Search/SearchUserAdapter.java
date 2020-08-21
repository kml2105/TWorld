package com.semonics.tworld.Search;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.semonics.tworld.R;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.CustomViewHolder > {

    public Context context;

    ArrayList<SearchUserModel> datalist;
    public interface OnItemClickListener {
        void onItemClick(View view, int postion, SearchUserModel item);
    }

    public SearchUserAdapter.OnItemClickListener listener;

    public SearchUserAdapter(Context context, ArrayList<SearchUserModel> arrayList, OnItemClickListener listener) {
        this.context = context;
        datalist= arrayList;
        this.listener=listener;
    }

    @Override
    public SearchUserAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_following,viewGroup,false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        SearchUserAdapter.CustomViewHolder viewHolder = new SearchUserAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image;
        TextView user_name;
        TextView user_id;
        TextView action_txt;
        RelativeLayout mainlayout;

        public CustomViewHolder(View view) {
            super(view);

            mainlayout=view.findViewById(R.id.mainlayout);

            user_image=view.findViewById(R.id.user_image);
            user_name=view.findViewById(R.id.user_name);
            user_id=view.findViewById(R.id.user_id);

            action_txt=view.findViewById(R.id.action_txt);
        }

        public void bind(final int pos , final SearchUserModel item, final SearchUserAdapter.OnItemClickListener listener) {



            mainlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,pos,item);
                }
            });

            action_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,pos,item);
                }
            });


        }


    }

    @Override
    public void onBindViewHolder(final SearchUserAdapter.CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);

        SearchUserModel item=datalist.get(i);

        holder.user_name.setText(item.firstName+" "+item.lastName);
/*
        Picasso.with(context)
                .load(item.p)
                .placeholder(R.drawable.user_profile)
                .into(holder.user_image);*/

        holder.user_id.setText(item.username);

        holder.action_txt.setVisibility(View.VISIBLE);

            if (item.status.equals("0")) {
                holder.action_txt.setText("Follow");
                holder.action_txt.setBackgroundColor(ContextCompat.getColor(context, R.color.redcolor));
                holder.action_txt.setTextColor(ContextCompat.getColor(context, R.color.white));
            }

            else {
                holder.action_txt.setText("UnFollow");
                holder.action_txt.setBackground(ContextCompat.getDrawable(context, R.drawable.d_gray_border));
                holder.action_txt.setTextColor(ContextCompat.getColor(context, R.color.black));
            }

        holder.bind(i,datalist.get(i),listener);

    }

}