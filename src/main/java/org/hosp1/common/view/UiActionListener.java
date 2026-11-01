package org.hosp1.common.view;

import java.util.Map;

@FunctionalInterface
public interface UiActionListener {
    void onAction(String action, Map<String, Object> data);
}
