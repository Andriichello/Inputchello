package com.andriichello.maskededittext.components;

import androidx.annotation.NonNull;

import com.andriichello.maskededittext.entries.EntryType;

public class Limiter {
    public Limiter() {
        //
    }

    public String limit(@NonNull Mask mask, String value) {
        if (value == null) {
            return null;
        }

        return !isOverfilled(mask, value) ? value
                : value.substring(0, mask.getTypeEntriesCount(EntryType.Replacement));
    }

    public int empties(@NonNull Mask mask, String value) {
        int replacements = mask.getTypeEntriesCount(EntryType.Replacement);
        if (replacements == 0) {
            return 0;
        }

        return value == null ? replacements : replacements - value.length();
    }

    public boolean isFilled(@NonNull Mask mask, String value) {
        return empties(mask, value) <= 0;
    }

    public boolean isOverfilled(@NonNull Mask mask, String value) {
        return empties(mask, value) < 0;
    }
}
