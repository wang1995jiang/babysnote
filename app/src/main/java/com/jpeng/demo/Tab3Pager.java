
package com.jpeng.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jpeng.demo.adapter.LearnAdapter;
import com.jpeng.demo.add.AddEvent;
import com.jpeng.demo.add.LearningNotes;
import com.jpeng.jptabbar.JPTabBar;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab3Pager extends Fragment implements View.OnClickListener {

    TextView addLearn;
    public RecyclerView recyclerView;
    LearnAdapter learnAdapter=new LearnAdapter(MainActivity.learns);
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.tab3,container,false);

        addLearn=(TextView) layout.findViewById(R.id.add_learn);
        recyclerView=(RecyclerView) layout.findViewById(R.id.recycle_learn);

        if (MainActivity.learns.size()==0){
            addLearn.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            addLearn.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            GridLayoutManager layoutManager=new GridLayoutManager(MainActivity.context,2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(learnAdapter);
        }
        addLearn.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_learn:
                Intent intent=new Intent(MyApplication.getContext(),LearningNotes.class);
                intent.putExtra("isAdd",true);
                startActivity(intent);
                break;
            default:break;
        }
    }

    public void toUpdate(){
        if (MainActivity.learns.size()==0){
            addLearn.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            if (MainActivity.learns.size()==1){
                addLearn.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                GridLayoutManager layoutManager=new GridLayoutManager(MainActivity.context,2);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(learnAdapter);
            }else {
                learnAdapter.notifyDataSetChanged();
            }
        }
    }
}
