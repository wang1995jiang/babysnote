package com.jpeng.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpeng.demo.adapter.CarmearAdapter;
import com.jpeng.demo.add.AddCarmera;
import com.jpeng.demo.add.LearningNotes;
import com.jpeng.jptabbar.JPTabBar;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab4Pager extends Fragment implements View.OnClickListener {

    TextView addCarmear;
    RecyclerView recyclerView;
    CarmearAdapter carmearAdapter=new CarmearAdapter(MainActivity.carmeras);
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.tab4,container,false);

        addCarmear=(TextView) layout.findViewById(R.id.camera_show);
        recyclerView=(RecyclerView) layout.findViewById(R.id.recycler_camera);

        if (MainActivity.carmeras.size()==0){
            addCarmear.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            addCarmear.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            GridLayoutManager layoutManager=new GridLayoutManager(MainActivity.context,1);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(carmearAdapter);
        }

        addCarmear.setOnClickListener(this);
        return layout;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera_show:
                Intent intent=new Intent(MyApplication.getContext(),AddCarmera.class);
                startActivity(intent);
                break;
            default:break;
        }
    }

    public void toUpdate(){
        if (MainActivity.carmeras.size()==0){
            addCarmear.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            if (MainActivity.carmeras.size()==1){
                addCarmear.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                GridLayoutManager layoutManager=new GridLayoutManager(MainActivity.context,1);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(carmearAdapter);
            }else {
                carmearAdapter.notifyDataSetChanged();
            }
        }
    }
}
