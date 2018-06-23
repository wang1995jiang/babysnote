package com.jpeng.demo;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jpeng.demo.adapter.CarmearAdapter;
import com.jpeng.demo.adapter.LearnAdapter;
import com.jpeng.demo.adapter.NoteAdapter;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.animate.AnimationType;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab2Pager extends Fragment{

    TextView noCollection;
    LinearLayout linearColl;
    ScrollView scrollView;

    NoteAdapter noteAdapter=new NoteAdapter(MainActivity.noteStars);
    LearnAdapter learnAdapter=new LearnAdapter(MainActivity.learnStars);
    CarmearAdapter carmearAdapter=new CarmearAdapter(MainActivity.carmeraStars);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout =inflater.inflate(R.layout.tab2,container,false);

        noCollection=(TextView) layout.findViewById(R.id.nohave_coll);
        scrollView=(ScrollView) layout.findViewById(R.id.scro_collection);
        linearColl=(LinearLayout) layout.findViewById(R.id.linear_collection);

        if (MainActivity.noteStars.size()==0&&MainActivity.learnStars.size()==0&&MainActivity.carmeraStars.size()==0){
            noCollection.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else {
            noCollection.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

            if (MainActivity.noteStars.size()>0){
                linearColl.addView(getRecyclerView(linearColl,0));
            }
            if (MainActivity.learnStars.size()>0){
                linearColl.addView(getRecyclerView(linearColl,1));
            }
            if (MainActivity.carmeraStars.size()>0){
                linearColl.addView(getRecyclerView(linearColl,2));
            }
        }

        return layout;
    }

    private View getRecyclerView(LinearLayout root,int id){
        View view=LayoutInflater.from(MainActivity.context).inflate(R.layout.recyle_layout,root,false);

        TextView name=(TextView) view.findViewById(R.id.name_recyle);
        RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.all_recycler);
        recyclerView.setNestedScrollingEnabled(false);

        switch (id){
            case 0:
                name.setText("星标记事");
                GridLayoutManager layoutManager=new GridLayoutManager(MainActivity.context,2);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(noteAdapter);
                view.setTag(0);
                break;
            case 1:
                name.setText("星标笔记");
                GridLayoutManager layoutManager1=new GridLayoutManager(MainActivity.context,2);
                recyclerView.setLayoutManager(layoutManager1);
                recyclerView.setAdapter(learnAdapter);
                view.setTag(1);
                break;
            case 2:
                name.setText("星标拍摄");
                GridLayoutManager layoutManager2=new GridLayoutManager(MainActivity.context,1);
                recyclerView.setLayoutManager(layoutManager2);
                recyclerView.setAdapter(carmearAdapter);
                view.setTag(2);
                break;
            default:break;
        }

        return view;
    }

    public void handleRecycler(int id,boolean isAdd){
        if (MainActivity.noteStars.size()==0&&MainActivity.learnStars.size()==0&&MainActivity.carmeraStars.size()==0){
            if (linearColl.getChildCount()>0){
                linearColl.removeAllViews();
            }
            noCollection.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else {
            noCollection.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

            switch (id){
                case 0:
                    if (MainActivity.noteStars.size()==0){
                        for (int i=0;i<linearColl.getChildCount();i++){
                            if (0==(int) linearColl.getChildAt(i).getTag()){
                                linearColl.removeView(linearColl.getChildAt(i));
                                break;
                            }
                        }
                    }else{
                        if (MainActivity.noteStars.size()==1){
                            if (isAdd){
                                linearColl.addView(getRecyclerView(linearColl,0));
                            }else {
                                noteAdapter.notifyDataSetChanged();
                            }
                        }else {
                            noteAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case 1:
                    if (MainActivity.learnStars.size()==0){
                        for (int i=0;i<linearColl.getChildCount();i++){
                            if (1==(int) linearColl.getChildAt(i).getTag()){
                                linearColl.removeView(linearColl.getChildAt(i));
                                break;
                            }
                        }
                    }else{
                        if (MainActivity.learnStars.size()==1){
                            if (isAdd){
                                linearColl.addView(getRecyclerView(linearColl,1));
                            }else {
                                learnAdapter.notifyDataSetChanged();
                            }
                        }else {
                            learnAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case 2:
                    if (MainActivity.carmeraStars.size()==0){
                        for (int i=0;i<linearColl.getChildCount();i++){
                            if (2==(int) linearColl.getChildAt(i).getTag()){
                                linearColl.removeView(linearColl.getChildAt(i));
                                break;
                            }
                        }
                    }else{
                        if (MainActivity.learnStars.size()==1){
                            if (isAdd){
                                linearColl.addView(getRecyclerView(linearColl,2));
                            }else {
                                carmearAdapter.notifyDataSetChanged();
                            }
                        }else {
                            carmearAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                default:break;
            }
        }
    }

}
