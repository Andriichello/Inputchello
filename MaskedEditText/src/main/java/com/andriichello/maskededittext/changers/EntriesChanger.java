package com.andriichello.maskededittext.changers;

import com.andriichello.maskededittext.actions.Action;
import com.andriichello.maskededittext.actions.Append;
import com.andriichello.maskededittext.actions.Insert;
import com.andriichello.maskededittext.actions.Prepend;
import com.andriichello.maskededittext.actions.Remove;
import com.andriichello.maskededittext.actions.Replace;
import com.andriichello.maskededittext.entries.Entries;
import com.andriichello.maskededittext.entries.Entry;
import com.andriichello.maskededittext.entries.EntryType;

public class EntriesChanger {

    public static Entries perform(Action action, Entries entries, EntryType type) {
        if (action instanceof Prepend) {
            return prepend((Prepend) action, type, entries);
        } else if (action instanceof Append) {
            return append((Append) action, type, entries);
        } else if (action instanceof Remove) {
            return remove((Remove) action, entries);
        } else if (action instanceof Insert) {
            return insert((Insert) action, type, entries);
        } else if (action instanceof Replace) {
            return replace((Replace) action, type, entries);
        }

        return entries;
    }

    public static Entries prepend(Prepend prepend, EntryType type, Entries entries) {
        if (entries == null) {
            return null;
        }

        for (int i = 0; i < prepend.value.length(); i++) {
            Entry entry = new Entry(prepend.value.substring(i, i + 1), type);
            entries.add(0, entry);
        }
        return entries;
    }

    public static Entries append(Append append, EntryType type, Entries entries) {
        if (entries == null) {
            return null;
        }

        for (int i = 0; i < append.value.length(); i++) {
            Entry entry = new Entry(append.value.substring(i, i + 1), type);
            entries.add(entry);
        }
        return entries;
    }

    public static Entries remove(Remove remove, Entries entries) {
        if (entries == null) {
            return null;
        }

        for (int i = remove.end - 1; i >= remove.beg; i--) {
            try {
                entries.remove(i);
            } catch (Exception e) {
                //
            }
        }

        return entries;
    }

    public static Entries insert(Insert insert, EntryType type, Entries entries) {
        if (entries == null) {
            return null;
        }

        if (insert.before == 0) {
            return prepend(new Prepend(insert.value), type, entries);
        }

        if (insert.before >= entries.size()) {
            return append(new Append(insert.value), type, entries);
        }

        for (int i = 0; i < insert.value.length(); i++) {
            entries.add(insert.before + i, new Entry(insert.value.substring(i, i + 1), type));
        }
        return entries;
    }

    public static Entries replace(Replace replace, EntryType type, Entries entries) {
        if (entries == null) {
            return null;
        }

        remove(new Remove(replace.beg, replace.end), entries);
        insert(new Insert(replace.beg, replace.value), type, entries);

        return entries;
    }
}
