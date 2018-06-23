package com.jpeng.demo.weather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.jpeng.demo.R;
import java.util.List;

/**
 * Created by 王将 on 2018/4/18.
 */

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.ViewHolder> {
    private List<Weather.HeWeather6Bean.HourlyBean> hourlyBeans;
    public HourAdapter(List<Weather.HeWeather6Bean.HourlyBean> hourlyBeanList){
        hourlyBeans=hourlyBeanList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_hour_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Weather.HeWeather6Bean.HourlyBean hourlyBean=hourlyBeans.get(position);
        holder.time.setText(ImageChose.getDivisionString(hourlyBean.getTime(),true));
        holder.tmp.setText(hourlyBean.getTmp()+"°C");
        String str=ImageChose.getDivisionString(hourlyBean.getTime(),true).substring(0,2);
        holder.icon.setImageResource(ImageChose.getWeatherImage(Integer.valueOf(hourlyBean.getCond_code()),str,true));

    }

    @Override
    public int getItemCount() {
        return hourlyBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView time,tmp;
        ImageView icon;
        public ViewHolder(View itemView) {
            super(itemView);
            time=(TextView)itemView.findViewById(R.id.time_hour_item);
            tmp=(TextView)itemView.findViewById(R.id.tmp_hour_item);
            icon=(ImageView)itemView.findViewById(R.id.icon_hour_item);
        }
    }
}
