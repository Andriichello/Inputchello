package com.andriichello.maskededittext.components;

import android.text.SpannableStringBuilder;

import androidx.annotation.NonNull;

import com.andriichello.maskededittext.entries.Entries;
import com.andriichello.maskededittext.entries.Entry;
import com.andriichello.maskededittext.entries.EntryType;

public class Composer {
    protected Mask mask;
    protected Limiter limiter;
    protected Disguiser disguiser;
    protected Colorizer colorizer;

    protected String value;
    protected Entries replaced;

    public Composer(@NonNull Mask mask, @NonNull Limiter limiter, @NonNull Disguiser disguiser, @NonNull Colorizer colorizer) {
        this.mask = mask;
        this.limiter = limiter;
        this.disguiser = disguiser;
        this.colorizer = colorizer;
    }

    public String getValue() {
        return value;
    }

    public Composer setValue(String str) {
        replaced = new Entries();
        value = limiter.limit(mask, str);

        if (str == null || str.isEmpty()) {
            return this;
        }

        Entries replacements = mask.getTypeEntries(EntryType.Replacement);
        for (int i = 0; i < str.length() && i < replacements.size(); i++) {
            Entry entry = new Entry(replacements.get(i));
            entry.type = EntryType.Value;
            entry.value = str.substring(i, i + 1);
            replaced.add(entry);
        }

        return this;
    }

    public Entries getReplaced() {
        return new Entries(replaced);
    }

    public void refresh() {
        setValue(value);
    }

    public Mask getMask() {
        return mask;
    }

    public Composer setMask(@NonNull Mask mask) {
        this.mask = mask;
        refresh();

        return this;
    }

    public Limiter getLimiter() {
        return limiter;
    }

    public Composer setLimiter(@NonNull Limiter limiter) {
        this.limiter = limiter;
        refresh();

        return this;
    }

    public Disguiser getDisguiser() {
        return disguiser;
    }

    public Composer setDisguiser(@NonNull Disguiser disguiser) {
        this.disguiser = disguiser;
        refresh();

        return this;
    }

    public Colorizer getColorizer() {
        return colorizer;
    }

    public Composer setColorizer(@NonNull Colorizer colorizer) {
        this.colorizer = colorizer;
        refresh();

        return this;
    }

    public Entries populate(Entries entries, Entries values) {
        Entries populated = new Entries(entries);
        if (values == null || values.isEmpty()) {
            return populated;
        }

        for (int i = 0, j = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);

            if (j < values.size() && entry.maskIndex.equals(values.get(j).maskIndex)) {
                Entry value = values.get(j++);

                entry.value = value.value;
                entry.type = value.type;
            }
        }
        return populated;
    }

    public Entries disguise(Entries entries) {
        Entries values = entries.only(EntryType.Value);
        String str = disguiser.disguise(mask, stringify(values));

        for (int i = 0; i < str.length() && i < values.size(); i++) {
            Entry entry = values.get(i);
            entry.value = str.substring(i, i + 1);
        }
        return entries;
    }

    public Entries getFilled(Entries entries) {
        return entries.takeUntil((entry, index) -> entry.isOfType(EntryType.Replacement));
    }

    public Entries getFilled(Entries entries, boolean shouldDisguise) {
        Entries filled = getFilled(entries);
        return shouldDisguise ? disguise(filled) : filled;
    }

    public Entries getUnfilled(Entries entries) {
        return entries.skipUntil((entry, index) -> !entry.isOfType(EntryType.Replacement));
    }

    public Entries getUnfilled(Entries entries, boolean keepHint) {
        return keepHint ? getUnfilled(entries) : new Entries();
    }

    public CharSequence getCharSequence(boolean keepHint, boolean shouldDisguise) {
        Entries entries = Entries.clone(mask.getEntries());
        Entries populated = populate(entries, replaced);

        Entries filled = getFilled(populated, shouldDisguise);
        Entries unfilled = getUnfilled(populated, keepHint);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(stringify(filled));
        builder.append(stringify(unfilled));

        return colorizer.colorize(builder, filled, unfilled);
    }

    public String stringify(Entries entries) {
        if (entries == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (Entry entry : entries) {
            builder.append(entry.value);
        }
        return builder.toString();
    }
}
