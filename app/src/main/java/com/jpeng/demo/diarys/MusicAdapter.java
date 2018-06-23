package com.jpeng.demo.diarys;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jpeng.demo.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 王将 on 2018/3/28.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private List<Music>mMusicList;
    private View.OnClickListener onClickListener;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        ImageView musicPicture;
        TextView musicTitle,musicAlbum,musicArtist,musicDuration,musicSize;
        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.music_id);
            musicPicture=(ImageView)itemView.findViewById(R.id.music_picture);
            musicTitle=(TextView)itemView.findViewById(R.id.music_title);
            musicAlbum=(TextView)itemView.findViewById(R.id.music_album);
            musicArtist=(TextView)itemView.findViewById(R.id.music_artist);
            musicDuration=(TextView)itemView.findViewById(R.id.music_duration);
            musicSize=(TextView)itemView.findViewById(R.id.music_size);
        }
    }

    public  MusicAdapter(List<Music> musicList,View.OnClickListener clickListener){
        mMusicList=musicList;
        onClickListener=clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music,parent,false);
        ViewHolder holder=new ViewHolder(view);
        holder.linearLayout.setOnClickListener(onClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Music music=mMusicList.get(position);
        holder.musicPicture.setImageBitmap(music.getPicture());
        holder.musicTitle.setText(music.getTitle());
        holder.musicAlbum.setText(music.getAlbum());
        holder.musicArtist.setText(music.getArtist());
        holder.musicDuration.setText(getMusicTime(music.getDuration()));
        holder.musicSize.setText(getMusicSize(music.getSize())+"M");
        holder.linearLayout.setTag(position);
    }


    @Override
    public int getItemCount() {
        return mMusicList.size();
    }

    public String getMusicTime(int second){
        String str="";
        int h,m,s,sAll;
        sAll=second/1000;
        if(sAll>3600){
            h=sAll/3600;
            m=(sAll-h*3600)/60;
            s=(sAll-h*3600)%60;
            str=h+":"+m+":"+s;
        }else {
           m=sAll/60;
           s=sAll%60;
           str=m+":"+s;
        }
        return str;
    }

    public String getMusicSize(Long size){
        double dSize=(double)size/(1024*1024);
        DecimalFormat df = new DecimalFormat("###.00");
        String result = df.format(dSize);
        return result;
    }
}
