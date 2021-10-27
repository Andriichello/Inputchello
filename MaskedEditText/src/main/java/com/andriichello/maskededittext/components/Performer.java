package com.andriichello.maskededittext.components;

import com.andriichello.maskededittext.actions.Action;
import com.andriichello.maskededittext.changers.StringChanger;

public class Performer {
    public String perform(Action action, String oldValue) {
        return StringChanger.perform(action, oldValue);
    }
}
