package com.jpeng.demo.diarys;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jpeng.demo.R;

import java.util.List;

/**
 * Created by 王将 on 2018/6/3.
 */

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {
    private Context mContext;
    private List<DiaryEntity> diaryEntities;

    public DiaryAdapter(List<DiaryEntity> diaryEntityList){
        diaryEntities=diaryEntityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_diary_layout,parent,false);

        ViewHolder holder=new ViewHolder(view);

        holder.relativeLayout.setOnLongClickListener(Diary.onLongClickListener);
        holder.relativeLayout.setOnClickListener(Diary.onClickListener);
        holder.star.setOnClickListener(Diary.onClickListener);
        holder.select.setOnClickListener(Diary.onClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DiaryEntity diaryEntity=diaryEntities.get(position);

        String []stringTime=diaryEntity.getTime().split(" ");

        holder.time.setText(stringTime[0]);
        holder.dayTime.setText(stringTime[1]);
        holder.mood.setText(diaryEntity.getMood());
        holder.weather.setText(diaryEntity.getWeather());
        holder.content.setText(diaryEntity.getContent());
        if (diaryEntity.isCollection()){
            holder.star.setImageResource(R.drawable.yes_star);
        }else {
            holder.star.setImageResource(R.drawable.no_star);
        }

        holder.star.setTag(diaryEntity);
        holder.relativeLayout.setTag(diaryEntity);

        if (Diary.deleteHandles.size()>0){
            int i=0;
            for (DeleteHandle deleteHandle:Diary.deleteHandles){
                if (diaryEntity.getId()!=deleteHandle.getDiaryEntity().getId()){
                    i++;
                }
            }
            if (i==Diary.deleteHandles.size()){
                DeleteHandle deleteHandle=new DeleteHandle();
                deleteHandle.setRelativeLayout(holder.relativeLayout);
                deleteHandle.setImageView(holder.select);
                deleteHandle.setDiaryEntity(diaryEntity);
                deleteHandle.setAscertain(false);
                Diary.deleteHandles.add(deleteHandle);
                holder.select.setTag(deleteHandle);
            }
        }else {
            DeleteHandle deleteHandle=new DeleteHandle();
            deleteHandle.setRelativeLayout(holder.relativeLayout);
            deleteHandle.setImageView(holder.select);
            deleteHandle.setDiaryEntity(diaryEntity);
            deleteHandle.setAscertain(false);
            Diary.deleteHandles.add(deleteHandle);
            holder.select.setTag(deleteHandle);
        }

        holder.select.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return diaryEntities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView time,mood,content,dayTime,weather;
        ImageView star,select;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView=(CardView) itemView;
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.rela_diary);
            time=(TextView) itemView.findViewById(R.id.item_diary_time);
            mood=(TextView) itemView.findViewById(R.id.item_diary_mood);
            content=(TextView) itemView.findViewById(R.id.item_diary_content);
            dayTime=(TextView) itemView.findViewById(R.id.day_time);
            weather=(TextView) itemView.findViewById(R.id.weather_diary);
            star=(ImageView) itemView.findViewById(R.id.item_diary_star);
            select=(ImageView) itemView.findViewById(R.id.select_show_diary);
        }
    }
}
