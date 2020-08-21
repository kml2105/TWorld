package com.semonics.tworld.Profile;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semonics.tworld.Home.HomeModel;
import com.semonics.tworld.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class MyVideos_Adapter extends RecyclerView.Adapter<MyVideos_Adapter.CustomViewHolder > {

    public Context context;
    private MyVideos_Adapter.OnItemClickListener listener;
    private ArrayList<HomeModel> dataList;


      public interface OnItemClickListener {
        void onItemClick(int postion, HomeModel item, View view);
    }

    public MyVideos_Adapter(Context context, ArrayList<HomeModel> dataList, MyVideos_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public MyVideos_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_myvideo_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        MyVideos_Adapter.CustomViewHolder viewHolder = new MyVideos_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }



    class CustomViewHolder extends RecyclerView.ViewHolder {


        ImageView thumb_image;

        TextView view_count;

        public CustomViewHolder(View view) {
            super(view);

            thumb_image=view.findViewById(R.id.thumb_image);
            view_count=view.findViewById(R.id.view_count);

        }

        public void bind(final int position, final HomeModel item, final MyVideos_Adapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position,item,v);
                }
            });

        }

    }




    @Override
    public void onBindViewHolder(final MyVideos_Adapter.CustomViewHolder holder, final int i) {
        final HomeModel item= dataList.get(i);
        holder.setIsRecyclable(false);



        try {
            Picasso.with(context).load(item.thumb)
                    .resize(100,100)
                    .placeholder(R.drawable.user_profile)
                    .into(holder.thumb_image);

        }catch (Exception e){

        }

        holder.view_count.setText(item.viewerCount+"");

        holder.bind(i,item,listener);

   }

}