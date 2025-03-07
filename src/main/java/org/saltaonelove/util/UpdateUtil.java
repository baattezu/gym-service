package org.saltaonelove.util;

import java.util.function.Consumer;

public class UpdateUtil {
    public static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
