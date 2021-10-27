package com.andriichello.maskededittext.entries;

import androidx.annotation.NonNull;

public class Entry {
    public String value;
    public EntryType type;
    public Integer typeIndex, maskIndex;

    public Entry(String value, EntryType type) {
        this.value = value;
        this.type = type;
    }

    public Entry(@NonNull Entry entry) {
        this(entry.value, entry.type);
        this.typeIndex = entry.typeIndex;
        this.maskIndex = entry.maskIndex;
    }

    public boolean isOfType(EntryType... types) {
        if (types == null || types.length == 0) {
            return true;
        }

        for (EntryType type : types) {
            if (this.type == type) {
                return true;
            }
        }
        return false;
    }
}
