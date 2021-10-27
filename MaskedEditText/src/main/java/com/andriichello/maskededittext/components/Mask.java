package com.andriichello.maskededittext.components;

import androidx.annotation.NonNull;

import com.andriichello.maskededittext.entries.Entries;
import com.andriichello.maskededittext.entries.EntryType;

import java.util.HashMap;
import java.util.Map;

public class Mask {
    protected String mask;
    protected String replacement;
    protected String initial;
    protected String allowed;
    protected String disguise;

    protected Entries entries;
    protected Map<EntryType, Entries> typeEntries;

    public Mask(@NonNull Options options, @NonNull Parser parser) {
        this.mask = options.getMask();
        this.replacement = options.getReplacement();
        this.initial = options.getInitial();
        this.allowed = options.getAllowed();
        this.disguise = options.getDisguise();

        parse(parser);
    }

    public Mask(@NonNull Mask mask) {
        this.mask = mask.getMask();
        this.replacement = mask.getReplacement();
        this.initial = mask.getInitial();
        this.allowed = mask.getAllowed();
        this.disguise = mask.getDisguise();

        this.entries = new Entries(mask.getEntries());
        this.typeEntries = new HashMap<>();
        for (EntryType type : mask.typeEntries.keySet()) {
            typeEntries.put(type, new Entries(mask.typeEntries.get(type)));
        }
    }

    protected void parse(@NonNull Parser parser) {
        entries = new Entries(parser.parse(mask, replacement, initial));

        typeEntries = new HashMap<>();
        for (EntryType type : EntryType.values()) {
            typeEntries.put(type, entries.filter((entry, integer) -> entry.isOfType(type)));
        }
    }

    public String getMask() {
        return mask;
    }

    public String getReplacement() {
        return replacement;
    }

    public String getInitial() {
        return initial;
    }

    public String getAllowed() {
        return allowed;
    }

    public String getDisguise() {
        return disguise;
    }

    public Entries getEntries() {
        return entries;
    }

    public int getEntriesCount() {
        return entries.size();
    }

    public Entries getTypeEntries(@NonNull EntryType type) {
        return typeEntries.get(type);
    }

    public int getTypeEntriesCount(@NonNull EntryType type) {
        Entries entries = getTypeEntries(type);
        return entries != null ? entries.size() : 0;
    }

    public boolean isEmpty() {
        return mask == null || mask.length() == 0 ||
                replacement == null || replacement.length() == 0;
    }

    public static class Options {
        protected String mask;
        protected String replacement;
        protected String initial;
        protected String allowed;
        protected String disguise;

        public Options(String mask, String replacement) {
            this.mask = mask;
            this.replacement = replacement;
        }

        public Options(String mask, String replacement, String initial) {
            this.mask = mask;
            this.replacement = replacement;
            this.initial = initial;
        }

        public Options(String mask, String replacement, String initial, String allowed) {
            this.mask = mask;
            this.replacement = replacement;
            this.initial = initial;
            this.allowed = allowed;
        }

        public Options(String mask, String replacement, String initial, String allowed, String disguise) {
            this.mask = mask;
            this.replacement = replacement;
            this.initial = initial;
            this.allowed = allowed;
            this.disguise = disguise;
        }

        public String getMask() {
            return mask;
        }

        public Options setMask(String mask) {
            this.mask = mask;
            return this;
        }

        public String getReplacement() {
            return replacement;
        }

        public Options setReplacement(String replacement) {
            this.replacement = replacement;
            return this;
        }

        public String getInitial() {
            return initial;
        }

        public Options setInitial(String initial) {
            this.initial = initial;
            return this;
        }

        public String getAllowed() {
            return allowed;
        }

        public Options setAllowed(String allowed) {
            this.allowed = allowed;
            return this;
        }

        public String getDisguise() {
            return disguise;
        }

        public Options getDisguise(String disguise) {
            this.allowed = disguise;
            return this;
        }
    }
}
