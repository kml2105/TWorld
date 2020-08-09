package com.semonics.tiktik.Home;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.semonics.tiktik.R;
import com.semonics.tiktik.WebService.SessionManager;
import com.semonics.tiktik.WebService.TicTic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.CustomViewHolder > {

    public Context context;
    private Home_Adapter.OnItemClickListener listener;
    private ArrayList<HomeModel> dataList;
    SessionManager sessionManager;


    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon, HomeModel item, View view);
    }



    public Home_Adapter(Context context, ArrayList<HomeModel> dataList, Home_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public Home_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        Home_Adapter.CustomViewHolder viewHolder = new Home_Adapter.CustomViewHolder(view);
        sessionManager = TicTic.getInstance().getSession();
        return viewHolder;
    }


    @Override
    public int getItemCount() {
       return dataList.size();
    }



    @Override
    public void onBindViewHolder(final Home_Adapter.CustomViewHolder holder, final int i) {
        final HomeModel item= dataList.get(i);
        holder.setIsRecyclable(false);

        try {

        holder.bind(i,item,listener);

            holder.username.setText(item.userDetails.firstName+" "+item.userDetails.lastName);
            holder.desc_txt.setText(item.hashTag+"\n" +item.caption);
            sessionManager.putString(SessionManager.PREF_LIKE_COUNT,item.likeCount.toString());
            holder.like_txt.setText(item.likeCount.toString());
            holder.comment_txt.setText(item.commentCount.toString());
            String videoURL = item.docName;
            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
                Uri videoURI = Uri.parse(videoURL);
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
                holder.playerview.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);
            } catch (Exception e) {
                Log.e("MainAcvtivity", " exoplayer error " + e.toString());
            }

            if((item.music.musicName==null || item.music.musicName.equals("") || item.music.musicName.equals("null"))){
                holder.sound_name.setText("original sound - "+item.userDetails.firstName+" "+item.userDetails.lastName);
            }else {
                holder.sound_name.setText(item.music.musicName);
            }
           holder.sound_name.setSelected(true);

        if(item.userDetails!=null){
            Picasso.with(context).
                    load(item.userDetails.profilePic)
                    .placeholder(context.getResources().getDrawable(R.drawable.user_profile))
                    .resize(100,100).into(holder.user_pic);
        }


            if(item.music!=null) {
                if((item.music.musicName==null || item.music.musicName.equals(""))
                        || item.music.musicName.equals("null")){

                    item.music.thumb=item.userDetails.profilePic;

                }
                else if(item.music.thumb.equals(""))
                    item.music.thumb="Null";


                Picasso.with(context).
                        load(item.music.thumb)
                        .placeholder(context.getResources().getDrawable(R.drawable.ic_round_music))
                        .resize(100,100).into(holder.sound_image);
            }


        if(item.like==1){
            holder.like_image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_fill));
        }
       else {
            holder.like_image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tap_to_like));
        }

        }catch (Exception e){

        }
   }



    class CustomViewHolder extends RecyclerView.ViewHolder {

        PlayerView playerview;
        TextView username,desc_txt,sound_name;
        ImageView user_pic,sound_image;

        LinearLayout like_layout,comment_layout,shared_layout,sound_image_layout;
        ImageView like_image,comment_image;
        TextView like_txt,comment_txt;


        public CustomViewHolder(View view) {
            super(view);

            playerview=view.findViewById(R.id.playerview);

            username=view.findViewById(R.id.username);
            user_pic=view.findViewById(R.id.user_pic);
            sound_name=view.findViewById(R.id.sound_name);
            sound_image=view.findViewById(R.id.sound_image);

            like_layout=view.findViewById(R.id.like_layout);
            like_image=view.findViewById(R.id.like_image);
            like_txt=view.findViewById(R.id.like_txt);

            desc_txt=view.findViewById(R.id.desc_txt);

            comment_layout=view.findViewById(R.id.comment_layout);
            comment_image=view.findViewById(R.id.comment_image);
            comment_txt=view.findViewById(R.id.comment_txt);


            sound_image_layout=view.findViewById(R.id.sound_image_layout);
            shared_layout=view.findViewById(R.id.shared_layout);
        }

        public void bind(final int postion, final HomeModel item, final Home_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });

            user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });


            like_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });


            comment_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });

            shared_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });

            sound_image_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        listener.onItemClick(postion,item,v);
                }
            });


        }


    }


}