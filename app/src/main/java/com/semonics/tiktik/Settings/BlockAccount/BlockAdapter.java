package com.semonics.tiktik.Settings.BlockAccount;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.semonics.tiktik.Model.BlockAccountModel;
import com.semonics.tiktik.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Komal on 3/20/2018.
 */

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.CustomViewHolder> {
    public Context context;

    ArrayList<BlockAccountModel> datalist;

    public interface OnItemClickListener {
        void onItemClick(View view, int postion, BlockAccountModel item);
    }

    public BlockAdapter.OnItemClickListener listener;

    public BlockAdapter(Context context, ArrayList<BlockAccountModel> arrayList, BlockAdapter.OnItemClickListener listener) {
        this.context = context;
        datalist = arrayList;
        this.listener = listener;
    }

    @Override
    public BlockAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_block_list, viewGroup, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        BlockAdapter.CustomViewHolder viewHolder = new BlockAdapter.CustomViewHolder(view);
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

            mainlayout = view.findViewById(R.id.mainlayout);

            user_image = view.findViewById(R.id.user_image);
            user_name = view.findViewById(R.id.user_name);
            user_id = view.findViewById(R.id.user_id);

            action_txt = view.findViewById(R.id.action_txt);
        }

        public void bind(final int pos, final BlockAccountModel item, final BlockAdapter.OnItemClickListener listener) {


            mainlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pos, item);
                }
            });

            action_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pos, item);
                }
            });


        }


    }

    @Override
    public void onBindViewHolder(final BlockAdapter.CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);

        BlockAccountModel item = datalist.get(i);

        if(item.first_name!=null && item.last_name!=null){
            holder.user_name.setText(item.first_name + " " + item.last_name);
        }else{
            holder.user_name.setText("User Name");
        }


        Picasso.with(context)
                .load(item.profile_pic)
                .placeholder(R.drawable.user_profile)
                .into(holder.user_image);

        holder.user_id.setText(item.username);

        holder.action_txt.setVisibility(View.VISIBLE);


        holder.bind(i, datalist.get(i), listener);

    }

}