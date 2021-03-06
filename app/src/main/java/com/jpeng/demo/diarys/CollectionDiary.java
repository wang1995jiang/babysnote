package com.jpeng.demo.diarys;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpeng.demo.R;

/**
 * Created by 王将 on 2018/3/27.
 */

public class CollectionDiary extends Fragment {
    TextView noShow;
    RecyclerView recyclerView;
    DiaryAdapter adapter=new DiaryAdapter(Diary.collections);
    GridLayoutManager layoutManager=new GridLayoutManager(Diary.context,2);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.collection_diary, container, false);

        noShow=(TextView) view.findViewById(R.id.no_collection);
        recyclerView=(RecyclerView) view.findViewById(R.id.collection_diary_recyler);

        if (Diary.collections.size()==0){
            noShow.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            noShow.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        }
        return view;
    }

    public void setCollectionChange(){
        if (Diary.collections.size()==0){
            noShow.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            if (Diary.collections.size()==1){
                noShow.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }else {
                adapter.notifyDataSetChanged();
            }
        }
    }

}
