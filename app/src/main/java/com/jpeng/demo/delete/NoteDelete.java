package com.jpeng.demo.delete;

import com.jpeng.demo.notes.Note;

/**
 * Created by 王将 on 2018/6/11.
 */

public class NoteDelete {
    private int position;
    private Note note;

    public void setNote(Note note) {
        this.note = note;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public Note getNote() {
        return note;
    }
}
