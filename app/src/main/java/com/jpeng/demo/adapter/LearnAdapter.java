package com.jpeng.demo.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jpeng.demo.MainActivity;
import com.jpeng.demo.R;
import com.jpeng.demo.delete.LearnDelete;
import com.jpeng.demo.notes.Learn;

import java.util.List;

/**
 * Created by 王将 on 2018/6/8.
 */

public class LearnAdapter extends RecyclerView.Adapter<LearnAdapter.ViewHolder> {

    private List<Learn> learns;
    private Context mContext;

    public LearnAdapter(List<Learn> learnList){
        learns=learnList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }

        View view= LayoutInflater.from(mContext).inflate(R.layout.iteam_learn_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);

        holder.relativeLayout.setOnLongClickListener(MainActivity.onLongClickListener);
        holder.relativeLayout.setOnClickListener(MainActivity.onClickListener);
        holder.star.setOnClickListener(MainActivity.onClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Learn learn=learns.get(position);

        String [] contents=learn.getContentLearn().split("&");
        String [] prices=learn.getPricesLearn().split(";");

        Glide.with(mContext).load(prices[0]).error(R.drawable.learn_backgound).centerCrop().into(holder.backGround);

        holder.time.setText(learn.getCreateTime());
        holder.content.setText(contents[0]);
        holder.label.setText(learn.getLabelLearn());
        if (learn.isCollection()){
            holder.star.setImageResource(R.drawable.yes_star);
        }else {
            holder.star.setImageResource(R.drawable.no_star);
        }

        LearnDelete learnDelete=new LearnDelete();
        learnDelete.setPosition(position);
        learnDelete.setLearn(learn);

        holder.star.setTag(learn);
        holder.relativeLayout.setTag(learnDelete);
    }

    @Override
    public int getItemCount() {
        return learns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView time,content,label;
        ImageView backGround,star;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView=(CardView) itemView;
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.item_rela_learn);
            time=(TextView) itemView.findViewById(R.id.time_learn);
            content=(TextView) itemView.findViewById(R.id.content_learn);
            label=(TextView) itemView.findViewById(R.id.label_learn);
            backGround=(ImageView) itemView.findViewById(R.id.image_learn);
            star=(ImageView) itemView.findViewById(R.id.star_learn);
        }
    }
}
