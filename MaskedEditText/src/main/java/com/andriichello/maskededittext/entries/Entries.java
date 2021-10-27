package com.andriichello.maskededittext.entries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class Entries extends ArrayList<Entry> {

    public Entries() {
        super();
    }

    public Entries(List<Entry> entries) {
        super();
        if (entries != null && !entries.isEmpty()) {
            this.addAll(entries);
        }
    }

    public Entries(Entries entries) {
        super();
        if (entries != null && !entries.isEmpty()) {
            this.addAll(entries);
        }
    }

    public Entries filter(BiPredicate<Entry, Integer> f) {
        Entries filtered = new Entries();
        for (int i = 0; i < size(); i++) {
            if (f != null && !f.test(get(i), i)) {
                continue;
            }
            filtered.add(get(i));
        }
        return filtered;
    }

    public Entries only(EntryType... types) {
        return filter((entry, index) -> entry.isOfType(types));
    }

    public Entries except(EntryType... types) {
        return filter((entry, index) -> !entry.isOfType(types));
    }

    public Entries each(BiFunction<Entry, Integer, Void> f) {
        if (f == null) {
            return this;
        }

        for (int i = 0; i < size(); i++) {
            f.apply(get(i), i);
        }
        return this;
    }

    public Entries reverse() {
        Entries reversed = new Entries();
        for (int i = size() - 1; i >= 0; i--) {
            reversed.add(get(i));
        }
        return reversed;
    }

    public Entries skip(int number) {
        if (number < 1) {
            return new Entries(this);
        }

        Entries entries = new Entries();
        for (int i = number; i < size(); i++) {
            entries.add(get(i));
        }

        return entries;
    }

    public Entries skipUntil(BiPredicate<Entry, Integer> f) {
        if (f == null) {
            return new Entries(this);
        }

        Entries entries = new Entries();
        for (int i = 0; i < size(); i++) {
            if (!f.test(get(i), i)) {
                return skip(i);
            }
        }
        return entries;
    }

    public Entries take(int number) {
        if (number < 1) {
            return new Entries();
        }

        Entries entries = new Entries();
        for (int i = 0; i < number; i++) {
            entries.add(get(i));
        }

        return entries;
    }

    public Entries takeUntil(BiPredicate<Entry, Integer> f) {
        if (f == null) {
            return new Entries(this);
        }

        Entries entries = new Entries();
        for (int i = 0; i < size(); i++) {
            if (f.test(get(i), i)) {
                break;
            }

            entries.add(get(i));
        }

        return entries;
    }

    public Entry first(BiPredicate<Entry, Integer> f) {
        if (isEmpty()) {
            return null;
        }
        return f == null ? get(0) : filter(f).first();
    }

    public Entry first() {
        return first(null);
    }

    public Entry last(BiPredicate<Entry, Integer> f) {
        if (isEmpty()) {
            return null;
        }
        return f == null ? get(size() - 1) : reverse().filter(f).first();
    }

    public Entry last() {
        return last(null);
    }

    public Entry next(int index, BiPredicate<Entry, Integer> f) {
        for (int i = Math.max(index, 0); i < size(); i++) {
            if (f != null && !f.test(get(i), i)) {
                continue;
            }
            return get(i);
        }
        return null;
    }

    public Entry next(int index) {
        return next(index, null);
    }

    public Entry prev(int index, BiPredicate<Entry, Integer> f) {
        for (int i = Math.min(index, size() - 1); i < size() && i >= 0; i--) {
            if (f != null && !f.test(get(i), i)) {
                continue;
            }
            return get(i);
        }
        return null;
    }

    public Entry prev(int index) {
        return prev(index, null);
    }

    public static Entry clone(Entry entry) {
        if (entry == null) {
            return null;
        }
        return new Entry(entry);
    }

    public static Entries clone(Entries entries) {
        Entries cloned = new Entries();
        for (Entry entry : entries) {
            cloned.add(clone(entry));
        }
        return cloned;
    }
}
