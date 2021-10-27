package com.andriichello.maskededittext.watchers;

public interface ValueWatcher {
    void onChange(String newValue, String oldValue, boolean isFilled);
}
