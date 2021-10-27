package com.andriichello.maskededittext.actions;

public class Replace extends Action {
    public int beg, end;

    public Replace(int beg, int end, String value) {
        super(value, ActionType.Replacing);

        this.beg = beg;
        this.end = end;
    }
}
