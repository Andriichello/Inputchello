package com.andriichello.maskededittext.components;

import androidx.annotation.NonNull;

import com.andriichello.maskededittext.actions.Action;
import com.andriichello.maskededittext.actions.Append;
import com.andriichello.maskededittext.actions.Insert;
import com.andriichello.maskededittext.actions.Prepend;
import com.andriichello.maskededittext.actions.Remove;
import com.andriichello.maskededittext.actions.Replace;
import com.andriichello.maskededittext.entries.Entries;
import com.andriichello.maskededittext.entries.Entry;
import com.andriichello.maskededittext.entries.EntryType;

public class Selectioner {
    public Integer selection(Action action, String oldValue, String newValue, @NonNull Mask mask) {
        Entries replacements = mask.getTypeEntries(EntryType.Replacement);

        if (action instanceof Append) {
            int length = newValue == null ? 0 : newValue.length();

            Entry next = replacements.next(length);
            return next == null ? mask.getEntriesCount() : next.maskIndex;
        }

        if (action instanceof Prepend) {
            int length = action.value == null ? 0 : action.value.length();

            Entry next = replacements.next(length);
            return next == null ? 0 : next.maskIndex;
        }

        if (action instanceof Insert) {
            int length = ((Insert) action).before + (action.value == null ? 0 : action.value.length());

            Entry next = replacements.next(length);
            return next == null ? mask.getEntriesCount() : next.maskIndex;
        }

        if (action instanceof Remove) {
            Entry next = replacements.prev(((Remove) action).beg);
            return next == null ? mask.getEntriesCount() : next.maskIndex;
        }

        if (action instanceof Replace) {
            int length = newValue == null ? 0 : newValue.length();
            int position = ((Replace) action).beg + (action.value == null ? 0 : action.value.length());

            Entry next = replacements.next(Math.min(length, position));
            return next == null ? mask.getEntriesCount() : next.maskIndex;
        }

        return null;
    }
}
