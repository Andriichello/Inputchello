package com.andriichello.maskededittext.components;

import androidx.annotation.NonNull;

public class Disguiser {
    public String disguise(@NonNull Mask mask, String value) {
        if (value == null || mask.getDisguise() == null || mask.getDisguise().isEmpty()) {
            return value;
        }

        String disguise = mask.getDisguise();
        if (disguise.length() > 1) {
            disguise = disguise.substring(0, 1);
        }

        StringBuilder disguised = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            disguised.append(disguise);
        }

        return disguised.toString();
    }
}
