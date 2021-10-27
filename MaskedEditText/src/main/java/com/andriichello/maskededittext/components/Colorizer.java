package com.andriichello.maskededittext.components;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.andriichello.maskededittext.entries.Entries;
import com.andriichello.maskededittext.entries.Entry;
import com.andriichello.maskededittext.entries.EntryType;

import java.util.HashMap;
import java.util.Map;

public class Colorizer {
    protected Integer textColor, hintColor;
    protected Map<EntryType, Integer> textColors, hintColors;

    public Colorizer(Integer textColor, Integer hintColor) {
        int capacity = EntryType.values().length;
        this.textColors = new HashMap<>(capacity);
        this.hintColors = new HashMap<>(capacity);

        setTextColor(textColor);
        setHintColor(hintColor);
    }

    public Integer getTextColor() {
        return textColor;
    }

    public void setTextColor(Integer color) {
        this.textColor = color;

        for (EntryType t : textColors.keySet()) {
            textColors.put(t, color);
        }
    }

    public Integer getHintColor() {
        return hintColor;
    }

    public Integer getHintColor(EntryType type) {
        if (type == null) {
            return getHintColor();
        }

        Integer color = hintColors.get(type);
        return color != null ? color : hintColor;
    }

    public void setHintColor(Integer color) {
        this.hintColor = color;

        for (EntryType t : hintColors.keySet()) {
            hintColors.put(t, color);
        }
    }

    public void setTextColor(Integer color, EntryType type) {
        if (type == null) {
            setTextColor(color);
            return;
        }

        textColors.put(type, color);
    }

    public Integer getTextColor(EntryType type) {
        if (type == null) {
            return getTextColor();
        }

        Integer color = textColors.get(type);
        return color != null ? color : textColor;
    }

    public void setHintColor(Integer color, EntryType type) {
        if (type == null) {
            setHintColor(color);
            return;
        }

        hintColors.put(type, color);
    }

    public SpannableStringBuilder colorize(SpannableStringBuilder output, Entries filled, Entries unfilled) {
        if (filled != null && !filled.isEmpty()) {
            for (Entry entry : filled) {
                Integer color = getTextColor(entry.type);
                if (color == null) {
                    continue;
                }

                ForegroundColorSpan span = new ForegroundColorSpan(color);
                output.setSpan(span, entry.maskIndex, entry.maskIndex + 1, 0);
            }
        }

        if (unfilled != null && !unfilled.isEmpty()) {
            int length = output.length();

            for (Entry entry : unfilled) {
                Integer color = getHintColor(entry.type);
                if (color == null || entry.maskIndex >= length) {
                    continue;
                }

                ForegroundColorSpan span = new ForegroundColorSpan(color);
                output.setSpan(span, entry.maskIndex, entry.maskIndex + 1, 0);
            }
        }
        return output;
    }
}
