package com.semonics.tworld.Notifications;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.semonics.tworld.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.CustomViewHolder> {
    public Context context;

    ArrayList<Notification_Get_Set> datalist;

    public interface OnItemClickListener {
        void onItemClick(View view, int postion, Notification_Get_Set item);
    }

    public Notification_Adapter.OnItemClickListener listener;

    public Notification_Adapter(Context context, ArrayList<Notification_Get_Set> arrayList, Notification_Adapter.OnItemClickListener listener) {
        this.context = context;
        datalist = arrayList;
        this.listener = listener;
    }

    @Override
    public Notification_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Notification_Adapter.CustomViewHolder viewHolder = new Notification_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageButton done;
        ImageView ivProfile;
        TextView tvMsg, tvUserName;


        public CustomViewHolder(View view) {
            super(view);
            //  image=view.findViewById(R.id.image);
            done = view.findViewById(R.id.done);
            tvMsg = view.findViewById(R.id.message);
            tvUserName = view.findViewById(R.id.username);
            ivProfile = view.findViewById(R.id.user_image);

        }

        public void bind(final int pos, final Notification_Get_Set item, final Notification_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pos, item);
                }
            });
        }

    }

    @Override
    public void onBindViewHolder(final Notification_Adapter.CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);
        holder.tvUserName.setText(datalist.get(i).username);
        holder.tvMsg.setText(datalist.get(i).title);
//        Picasso.with(context).load(datalist.get(i).profile_pic).centerCrop().into(holder.ivProfile);
        holder.bind(i, datalist.get(i), listener);

    }

}