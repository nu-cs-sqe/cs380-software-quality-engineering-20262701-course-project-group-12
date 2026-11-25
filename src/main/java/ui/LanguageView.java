package ui;

import java.awt.GraphicsEnvironment;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;

public class LanguageView {
    private static final int DEFAULT_CHOICE = 0;

    private LanguageView() {
    }

    static Locale choose() {
        List<Locale> locales = Messages.availableLocales();
        if (GraphicsEnvironment.isHeadless()) {
            return locales.get(DEFAULT_CHOICE);
        }
        String[] names = locales.stream().map(Messages::languageName).toArray(String[]::new);
        int choice = JOptionPane.showOptionDialog(
                null,
                Messages.text("language.prompt"),
                Messages.text("language.title"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                names,
                names[DEFAULT_CHOICE]);
        if (choice == JOptionPane.CLOSED_OPTION) {
            return locales.get(DEFAULT_CHOICE);
        }
        return locales.get(choice);
    }
}
