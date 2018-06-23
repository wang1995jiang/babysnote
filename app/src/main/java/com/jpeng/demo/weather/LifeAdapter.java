package com.jpeng.demo.weather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jpeng.demo.R;
import java.util.List;

/**
 * Created by 王将 on 2018/4/20.
 */

public class LifeAdapter extends RecyclerView.Adapter<LifeAdapter.ViewHolder> {
    List<Weather.HeWeather6Bean.LifestyleBean> lifestyleBeans;

    public LifeAdapter(List<Weather.HeWeather6Bean.LifestyleBean> lifestyleBeanList){
        lifestyleBeans=lifestyleBeanList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_life_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Weather.HeWeather6Bean.LifestyleBean lifestyleBean=lifestyleBeans.get(position);
        holder.brf.setText(ImageChose.getBrfTxt(lifestyleBean.getType()));
        holder.brfTxt.setText(lifestyleBean.getBrf());
        holder.txt.setText(lifestyleBean.getTxt());
    }

    @Override
    public int getItemCount() {
        return lifestyleBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView brf,txt,brfTxt;
        public ViewHolder(View itemView) {
            super(itemView);
            brf=(TextView)itemView.findViewById(R.id.brf_life);
            txt=(TextView)itemView.findViewById(R.id.txt_life);
            brfTxt=(TextView)itemView.findViewById(R.id.brf);
        }
    }
}
