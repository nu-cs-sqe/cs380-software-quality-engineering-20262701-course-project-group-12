package ui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

final class Messages {
    private static final ResourceBundle.Control PROPERTIES_WITHOUT_DEFAULT_FALLBACK =
            ResourceBundle.Control.getNoFallbackControl(
                    ResourceBundle.Control.FORMAT_PROPERTIES);

    private static ResourceBundle bundle = load(Locale.ENGLISH);

    private Messages() {
    }

    static void useLocale(Locale locale) {
        bundle = load(locale);
    }

    static String text(String key) {
        return bundle.getString(key);
    }

    static String format(String key, Object... arguments) {
        return MessageFormat.format(text(key), arguments);
    }

    private static ResourceBundle load(Locale locale) {
        return ResourceBundle.getBundle("messages", locale, PROPERTIES_WITHOUT_DEFAULT_FALLBACK);
    }
}
