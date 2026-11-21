package ui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

final class Messages {
    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    private Messages() {
    }

    static void useLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    static String text(String key) {
        return bundle.getString(key);
    }

    static String format(String key, Object... arguments) {
        return MessageFormat.format(text(key), arguments);
    }
}
