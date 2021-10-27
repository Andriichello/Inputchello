package com.andriichello.maskededittext.actions;

public class Remove extends Action {
    public int beg, end;

    public Remove(int beg, int end) {
        super(null, ActionType.Removing);

        this.beg = beg;
        this.end = end;
    }
}
