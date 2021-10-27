package com.andriichello.maskededittext.changers;

import com.andriichello.maskededittext.actions.Action;
import com.andriichello.maskededittext.actions.Append;
import com.andriichello.maskededittext.actions.Insert;
import com.andriichello.maskededittext.actions.Prepend;
import com.andriichello.maskededittext.actions.Remove;
import com.andriichello.maskededittext.actions.Replace;

public class StringChanger {

    public static String perform(Action action, String target) {
        target = value(target);

        if (action instanceof Prepend) {
            return prepend((Prepend) action, target);
        } else if (action instanceof Append) {
            return append((Append) action, target);
        } else if (action instanceof Remove) {
            return remove((Remove) action, target);
        } else if (action instanceof Insert) {
            return insert((Insert) action, target);
        } else if (action instanceof Replace) {
            return replace((Replace) action, target);
        }

        return target;
    }

    public static String value(String string) {
        return string != null ? string : "";
    }

    public static String value(Action action) {
        return action.value != null ? action.value : "";
    }

    public static String prepend(Prepend prepend, String target) {
        return value(prepend) + value(target);
    }

    public static String append(Append append, String target) {
        return value(target) + value(append);
    }

    public static String remove(Remove remove, String target) {
        int beg = Math.max(remove.beg, 0);
        int end = Math.min(remove.end, target.length());

        return beg < end ? target.substring(0, beg) + target.substring(end) : target;
    }

    public static String insert(Insert insert, String target) {
        if (insert.before == 0) {
            return prepend(new Prepend(insert.value), target);
        }

        int length = target.length();

        if (insert.before >= length) {
            return append(new Append(insert.value), target);
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == insert.before) {
                builder.append(value(insert));
            }
            builder.append(target.charAt(i));
        }

        return builder.toString();
    }

    public static String replace(Replace replace, String target) {
        target = remove(new Remove(replace.beg, replace.end), target);
        target = insert(new Insert(replace.beg, replace.value), target);

        return target;
    }
}
