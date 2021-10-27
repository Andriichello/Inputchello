package com.andriichello.maskededittext.actions;

public abstract class Action {
    public String value;
    public final ActionType type;

    public Action(String value, ActionType type) {
        this.value = value;
        this.type = type;
    }
}
