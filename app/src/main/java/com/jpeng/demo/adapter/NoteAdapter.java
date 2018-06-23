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

import com.jpeng.demo.MainActivity;
import com.jpeng.demo.R;
import com.jpeng.demo.delete.NoteDelete;
import com.jpeng.demo.notes.Note;

import java.util.List;


/**
 * Created by 王将 on 2018/6/7.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> notes;
    private Context mContext;

    public NoteAdapter(List<Note> noteList){
        notes=noteList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }

        View view= LayoutInflater.from(mContext).inflate(R.layout.iteam_note_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);

        holder.linearLayout.setOnLongClickListener(MainActivity.onLongClickListener);
        holder.linearLayout.setOnClickListener(MainActivity.onClickListener);
        holder.star.setOnClickListener(MainActivity.onClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note=notes.get(position);

        holder.time.setText(note.getCreateTime());
        holder.content.setText(note.getContentNote());
        holder.label.setText(note.getLabelNote());
        if (note.isCollection()){
            holder.star.setImageResource(R.drawable.yes_star);
        }else {
            holder.star.setImageResource(R.drawable.no_star);
        }

        NoteDelete noteDelete=new NoteDelete();
        noteDelete.setPosition(position);
        noteDelete.setNote(note);

        holder.star.setTag(note);
        holder.linearLayout.setTag(noteDelete);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView time,content,label;
        ImageView star;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView=(CardView) itemView;
            linearLayout=(LinearLayout) itemView.findViewById(R.id.item_note_linear) ;
            time=(TextView) itemView.findViewById(R.id.time_note);
            content=(TextView) itemView.findViewById(R.id.content_note);
            label=(TextView) itemView.findViewById(R.id.label_note);
            star=(ImageView) itemView.findViewById(R.id.star_note);
        }
    }
}
