package com.andriichello.maskededittext.components;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filterer {
    public String filter(@NonNull String value, String regex) {
        if (regex == null) {
            return value;
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            builder.append(value.substring(matcher.start(), matcher.end()));
        }
        return builder.toString();
    }
}
