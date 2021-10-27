package com.andriichello.maskededittext.components;

import com.andriichello.maskededittext.entries.Entry;
import com.andriichello.maskededittext.entries.EntryType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public List<Entry> parse(String mask, String replacement) {
        return parse(mask, replacement);
    }

    public List<Entry> parse(String mask, String replacement, String initial) {
        List<Entry> entries = new ArrayList<>();
        if (mask == null || mask.isEmpty() || replacement == null || replacement.isEmpty()) {
            return entries;
        }

        for (int i = 0, f = 0, r = 0; i < mask.length(); i++) {
            if (mask.startsWith(replacement, i)) {
                String value = (initial != null && initial.length() > r)
                        ? initial.substring(r, r + 1) : replacement;

                Entry entry = new Entry(value, EntryType.Replacement);
                entry.maskIndex = i;
                entry.typeIndex = r++;
                entries.add(entry);

                i += replacement.length() - 1;
                continue;
            }

            Entry entry = new Entry(mask.substring(i, i + 1), EntryType.Filler);
            entry.maskIndex = i;
            entry.typeIndex = f++;
            entries.add(entry);
        }

        return entries;
    }
}
