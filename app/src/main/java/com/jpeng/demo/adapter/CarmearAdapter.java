package com.jpeng.demo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jpeng.demo.MainActivity;
import com.jpeng.demo.R;
import com.jpeng.demo.delete.CarmearDelete;
import com.jpeng.demo.notes.Carmera;

import java.util.List;

/**
 * Created by 王将 on 2018/6/9.
 */

public class CarmearAdapter extends RecyclerView.Adapter<CarmearAdapter.ViewHolder> {
    private List<Carmera> carmeras;
    private Context mContext;

    public CarmearAdapter(List<Carmera> carmeraList){
        carmeras=carmeraList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }

        View view= LayoutInflater.from(mContext).inflate(R.layout.iteam_carmear_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);

        holder.linearLayout.setOnLongClickListener(MainActivity.onLongClickListener);
        holder.star.setOnClickListener(MainActivity.onClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Carmera carmera=carmeras.get(position);

        holder.time.setText(carmera.getCreateTime());
        holder.label.setText(carmera.getLabelCarmera());
        holder.content.setText(carmera.getMoodCarmera());
        holder.address.setText(carmera.getAddressCarmera());
        Glide.with(mContext).load(carmera.getPathCarmera()).error(R.drawable.no_picture).into(holder.photo);
        if (carmera.isCollection()){
            holder.star.setImageResource(R.drawable.yes_star);
        }else {
            holder.star.setImageResource(R.drawable.no_star);
        }

        CarmearDelete carmearDelete=new CarmearDelete();
        carmearDelete.setPosition(position);
        carmearDelete.setCarmera(carmera);

        holder.star.setTag(carmera);
        holder.linearLayout.setTag(carmearDelete);
    }

    @Override
    public int getItemCount() {
        return carmeras.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView time,label,content,address;
        ImageView photo,star;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);

            cardView=(CardView) itemView;
            linearLayout=(LinearLayout) itemView.findViewById(R.id.linear_carmear);
            time=(TextView) itemView.findViewById(R.id.time_carmear);
            label=(TextView) itemView.findViewById(R.id.label_carmear);
            content=(TextView) itemView.findViewById(R.id.content_carmear);
            address=(TextView) itemView.findViewById(R.id.address_carmear);
            photo=(ImageView) itemView.findViewById(R.id.photo_carmear);
            star=(ImageView) itemView.findViewById(R.id.star_carmear);
        }
    }
}
