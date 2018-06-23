package com.jpeng.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jpeng.demo.adapter.NoteAdapter;
import com.jpeng.demo.add.AddEvent;
import com.jpeng.demo.diarys.Diary;
import com.jpeng.jptabbar.JPTabBar;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab1Pager extends Fragment implements View.OnClickListener {

    TextView addEvent;
    public RecyclerView recyclerView;
    NoteAdapter noteAdapter=new NoteAdapter(MainActivity.noteList);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.tab1, container,false);
        MyApplication.getJpTabBar().hideBadge(0);

        addEvent=(TextView)layout.findViewById(R.id.add_event);
        recyclerView=(RecyclerView) layout.findViewById(R.id.recycler_note);

        if (MainActivity.noteList.size()==0){
            addEvent.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            addEvent.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            GridLayoutManager layoutManager=new GridLayoutManager(MainActivity.context,2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(noteAdapter);
        }

        addEvent.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_event:
                Intent intent=new Intent(MyApplication.getContext(),AddEvent.class);
                intent.putExtra("isAdd",true);
                startActivity(intent);
                break;
            default:break;
        }
    }

    public void toUpdate(){
        if (MainActivity.noteList.size()==0){
            addEvent.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            if (MainActivity.noteList.size()==1){
                addEvent.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                GridLayoutManager layoutManager=new GridLayoutManager(MainActivity.context,2);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(noteAdapter);
            }else {
                noteAdapter.notifyDataSetChanged();
            }
        }
    }
}
