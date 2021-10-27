package com.andriichello.maskededittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.andriichello.maskededittext.actions.Action;
import com.andriichello.maskededittext.actions.Append;
import com.andriichello.maskededittext.actions.Insert;
import com.andriichello.maskededittext.actions.Prepend;
import com.andriichello.maskededittext.actions.Remove;
import com.andriichello.maskededittext.actions.Replace;
import com.andriichello.maskededittext.components.Colorizer;
import com.andriichello.maskededittext.components.Composer;
import com.andriichello.maskededittext.components.Disguiser;
import com.andriichello.maskededittext.components.Filterer;
import com.andriichello.maskededittext.components.Limiter;
import com.andriichello.maskededittext.components.Mask;
import com.andriichello.maskededittext.components.Parser;
import com.andriichello.maskededittext.components.Performer;
import com.andriichello.maskededittext.components.Selectioner;
import com.andriichello.maskededittext.entries.Entries;
import com.andriichello.maskededittext.entries.Entry;
import com.andriichello.maskededittext.entries.EntryType;
import com.andriichello.maskededittext.watchers.ValueWatcher;

public class MaskedEditText extends AppCompatEditText
        implements TextWatcher {

    protected Mask mask;
    protected Composer composer;

    protected Limiter limiter;
    protected Filterer filterer;
    protected Performer performer;
    protected Disguiser disguiser;
    protected Colorizer colorizer;
    protected Selectioner selectioner;

    protected Action action;
    protected Integer selection;
    protected Integer selectionStart, selectionEnd;

    protected boolean editing;
    protected boolean keepHint;
    protected boolean shouldDisguise;
    protected boolean initialized;

    protected ValueWatcher valueWatcher;

    public MaskedEditText(@NonNull Context context) {
        super(context);
        initialize(context, null);
    }

    public MaskedEditText(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public MaskedEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public void initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MaskedEditText,
                0, 0);

        String mask = null, replacement = null, initial = null, allowed = null, disguise = null;
        try {
            mask = a.getString(R.styleable.MaskedEditText_mask);
            replacement = a.getString(R.styleable.MaskedEditText_replacement);
            initial = a.getString(R.styleable.MaskedEditText_initial);
            allowed = a.getString(R.styleable.MaskedEditText_allowed);
            disguise = a.getString(R.styleable.MaskedEditText_disguise);
            keepHint = a.getBoolean(R.styleable.MaskedEditText_keepHint, true);
            shouldDisguise = a.getBoolean(R.styleable.MaskedEditText_shouldDisguise, false);
        } catch (Exception exception) {
            //
        }
        a.recycle();

        Mask.Options options = new Mask.Options(mask, replacement, initial, allowed, disguise);
        this.mask = new Mask(options, new Parser());
        this.composer = new Composer(
                this.mask,
                this.limiter = new Limiter(),
                this.disguiser = new Disguiser(),
                this.colorizer = new Colorizer(getCurrentTextColor(), getCurrentHintTextColor())
        );

        this.filterer = new Filterer();
        this.performer = new Performer();
        this.selectioner = new Selectioner();

        this.initialized = true;

        this.addTextChangedListener(this);
        setValue(getText() == null ? "" : getText().toString());
    }

    public ValueWatcher getValueWatcher() {
        return valueWatcher;
    }

    public MaskedEditText setValueWatcher(ValueWatcher valueWatcher) {
        this.valueWatcher = valueWatcher;
        return this;
    }

    public void setText(CharSequence text, boolean editing) {
        this.editing = editing;
        setText(text);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        this.colorizer.setTextColor(color);
    }

    public void setHintColor(int color) {
        super.setHintTextColor(color);
        this.colorizer.setHintColor(color);
    }

    public String getValue() {
        return composer.getValue();
    }

    public MaskedEditText setValue(String newValue, boolean isRefreshing) {
        String oldValue = getValue();

        selection = selectioner.selection(action, oldValue, newValue, mask);
        selection = selection != null ? selection : getSelectionStart();

        CharSequence composed = composer.setValue(newValue)
                .getCharSequence(keepHint, shouldDisguise);

        setText(composed, true);

        if (!isRefreshing && valueWatcher != null) {
            valueWatcher.onChange(newValue, oldValue, limiter.isFilled(mask, newValue));
        }

        return this;
    }

    public MaskedEditText setValue(String newValue) {
        return setValue(newValue, false);
    }

    public void refresh() {
        setValue(getValue(), true);
    }

    public boolean isKeepHint() {
        return keepHint;
    }

    public MaskedEditText setKeepHint(boolean keepHint) {
        this.keepHint = keepHint;
        return this;
    }

    public boolean shouldDisguise() {
        return keepHint;
    }

    public MaskedEditText setShouldDisguise(boolean shouldDisguise) {
        this.shouldDisguise = shouldDisguise;
        return this;
    }

    public Mask getMask() {
        return mask;
    }

    public MaskedEditText setMask(@NonNull Mask mask) {
        this.mask = mask;
        this.composer.setMask(mask);

        return this;
    }

    public Limiter getLimiter() {
        return limiter;
    }

    public MaskedEditText setLimiter(@NonNull Limiter limiter) {
        this.limiter = limiter;
        this.composer.setLimiter(limiter);

        return this;
    }

    public Filterer getFilterer() {
        return filterer;
    }

    public MaskedEditText setFilterer(@NonNull Filterer filterer) {
        this.filterer = filterer;
        return this;
    }

    public Performer getPerformer() {
        return performer;
    }

    public MaskedEditText setPerformer(@NonNull Performer performer) {
        this.performer = performer;
        return this;
    }

    public Disguiser getDisguiser() {
        return disguiser;
    }

    public MaskedEditText setDisguiser(@NonNull Disguiser disguiser) {
        this.disguiser = disguiser;
        return this;
    }

    public Selectioner getSelectioner() {
        return selectioner;
    }

    public MaskedEditText setSelectioner(@NonNull Selectioner selectioner) {
        this.selectioner = selectioner;
        return this;
    }

    public Colorizer getColorizer() {
        return colorizer;
    }

    public MaskedEditText setColorizer(@NonNull Colorizer colorizer) {
        this.colorizer = colorizer;
        this.composer.setColorizer(colorizer);
        return this;
    }

    public Action adding(CharSequence s, int start, int count) {
        String part = s.subSequence(start, start + count).toString();

        int empties = limiter.empties(mask, getValue());
        int replaces = mask.getTypeEntriesCount(EntryType.Replacement);
        if (start == 0 || empties == replaces) {
            return new Prepend(part);
        }

        Entries replacements = mask.getTypeEntries(EntryType.Replacement);
        Entry next = replacements.first((entry, integer) -> entry.maskIndex >= start);
        if (next == null || next.typeIndex >= replaces - empties) {
            return new Append(part);
        }

        return new Insert(next.typeIndex, part);
    }

    public Action removing(CharSequence s, int start, int before) {
        if (getValue() == null || getValue().isEmpty()) {
            return new Remove(0, 0);
        }
        int length = getValue().length();

        Entries selected = mask.getEntries()
                .skip(start).take(before);

        Entries nonFillers = selected.except(EntryType.Filler);
        if (nonFillers.size() > 0) {
            // if there are any selected replacements then only they should be removed
            Entry first = nonFillers.first();
            Entry last = nonFillers.last();

            if (first != null && first.typeIndex < length) {
                return new Remove(first.typeIndex, last.typeIndex + 1);
            }
        }

        // finds the last char of value that can be removed
        Entry toBeRemoved = mask.getTypeEntries(EntryType.Replacement)
                .last((entry, index) -> entry.maskIndex < start
                        && entry.typeIndex < length);

        if (selectionStart == null || !selectionStart.equals(selectionEnd)) {
            // nothing should be removed but cursor should be at the first empty replacement
            return new Remove(toBeRemoved.typeIndex + 1, toBeRemoved.typeIndex + 1);
        }
        return new Remove(toBeRemoved.typeIndex, toBeRemoved.typeIndex + 1);
    }

    public Action replacing(CharSequence s, int start, int before, int count) {
        String part = s.subSequence(start, start + count).toString();
        if (getValue() == null || getValue().isEmpty()) {
            return new Append(part);
        }
        int length = getValue().length();

        Entries selected = mask.getEntries()
                .skip(start).take(before);

        Entries nonFillers = selected.except(EntryType.Filler);
        if (nonFillers.size() > 0) {
            // if there are any selected replacements then only they should be removed
            Entry first = nonFillers.first();
            Entry last = nonFillers.last();

            if (first != null && first.typeIndex < length) {
                return new Replace(first.typeIndex, last.typeIndex + 1, part);
            }
        }

        return new Append(part);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (selectionStart == null || selectionEnd == null) {
            selectionStart = getSelectionStart();
            selectionEnd = getSelectionEnd();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (editing || !initialized) {
            return;
        }

        // all action indexes are translated from mask to value
        if (before > 0 && count > 0) {
            action = replacing(s, start, before, count);
        } else if (before > 0) {
            action = removing(s, start, before);
        } else if (count > 0) {
            action = adding(s, start, count);
        }

        if (action != null && action.value != null) {
            // filter not allowed chars from action value
            action.value = filterer.filter(action.value, mask.getAllowed());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (selection != null && getSelectionStart() != selection) {
            setSelection(Math.min(selection, s.length()));
        }

        if (action == null || editing || !initialized) {
            selectionStart = null;
            selectionEnd = null;
            selection = null;
            editing = false;
            action = null;
            return;
        }

        setValue(performer.perform(action, getValue()));
    }
}
