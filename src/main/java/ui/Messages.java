package ui;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Messages {
    private static final ResourceBundle.Control PROPERTIES_WITHOUT_DEFAULT_FALLBACK =
            ResourceBundle.Control.getNoFallbackControl(
                    ResourceBundle.Control.FORMAT_PROPERTIES);

    private static final Locale ROOT_LANGUAGE = Locale.ENGLISH;
    private static final String LANGUAGE_NAME_KEY = "language.name";

    private static ResourceBundle bundle = load(ROOT_LANGUAGE);

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

    static List<Locale> availableLocales() {
        Stream<Locale> fileLanguages = Arrays.stream(Locale.getISOLanguages())
                .map(Locale::new)
                .filter(Messages::hasOwnFile);
        return Stream.concat(Stream.of(ROOT_LANGUAGE), fileLanguages)
                .collect(Collectors.toList());
    }

    static String languageName(Locale language) {
        return load(language).getString(LANGUAGE_NAME_KEY);
    }

    private static boolean hasOwnFile(Locale language) {
        return load(language).getLocale().equals(language);
    }

    private static ResourceBundle load(Locale locale) {
        return ResourceBundle.getBundle("messages", locale, PROPERTIES_WITHOUT_DEFAULT_FALLBACK);
    }
}
