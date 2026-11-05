package ui;

import java.text.MessageFormat;
import java.util.ResourceBundle;

final class Messages {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages");

    private Messages() {
    }

    static String text(String key) {
        return BUNDLE.getString(key);
    }
}
