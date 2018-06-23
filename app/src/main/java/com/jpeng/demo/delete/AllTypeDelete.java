package com.jpeng.demo.delete;

import com.jpeng.demo.notes.Carmera;
import com.jpeng.demo.notes.Learn;
import com.jpeng.demo.notes.Note;

/**
 * Created by 王将 on 2018/6/12.
 */

public class AllTypeDelete {
    private int id;
    private Note note;
    private Learn learn;
    private Carmera carmera;

    public void setLearn(Learn learn) {
        this.learn = learn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public void setCarmera(Carmera carmera) {
        this.carmera = carmera;
    }

    public Learn getLearn() {
        return learn;
    }

    public int getId() {
        return id;
    }

    public Note getNote() {
        return note;
    }

    public Carmera getCarmera() {
        return carmera;
    }
}
