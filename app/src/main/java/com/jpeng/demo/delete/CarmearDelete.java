package com.jpeng.demo.delete;

import com.jpeng.demo.notes.Carmera;

/**
 * Created by 王将 on 2018/6/13.
 */

public class CarmearDelete {
    private int position;
    private Carmera carmera;

    public void setCarmera(Carmera carmera) {
        this.carmera = carmera;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Carmera getCarmera() {
        return carmera;
    }

    public int getPosition() {
        return position;
    }
}
