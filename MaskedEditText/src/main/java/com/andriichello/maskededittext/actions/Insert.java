package com.andriichello.maskededittext.actions;

public class Insert extends Action {
    public int before;

    public Insert(int before, String value) {
        super(value, ActionType.Adding);
        this.before = before;
    }
}
