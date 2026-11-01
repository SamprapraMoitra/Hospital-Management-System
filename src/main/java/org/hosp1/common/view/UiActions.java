package org.hosp1.common.view;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class UiActions {

    private UiActions() {
    }

    public static Map<String, Object> data(Object... entries) {
        if (entries.length % 2 != 0) {
            throw new IllegalArgumentException("Action data must contain key/value pairs.");
        }

        Map<String, Object> data = new LinkedHashMap<>();
        for (int i = 0; i < entries.length; i += 2) {
            if (!(entries[i] instanceof String key)) {
                throw new IllegalArgumentException("Action data keys must be strings.");
            }
            data.put(key, entries[i + 1]);
        }
        return data;
    }

    public static void send(UiActionListener listener, String action,
                            Map<String, Object> data) {
        Objects.requireNonNull(listener, "listener").onAction(action, data);
    }
}
