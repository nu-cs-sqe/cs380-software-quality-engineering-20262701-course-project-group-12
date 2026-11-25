package ui;

import java.awt.GraphicsEnvironment;
import java.util.Locale;
import javax.swing.JOptionPane;

public class LanguageView {
    private static final int ENGLISH_CHOICE = 0;
    private static final Locale[] LOCALES = {Locale.ENGLISH, Locale.FRENCH};

    private LanguageView() {
    }

    static Locale choose() {
        if (GraphicsEnvironment.isHeadless()) {
            return Locale.ENGLISH;
        }
        String english = Messages.text("language.english");
        String french = Messages.text("language.french");
        String[] labels = {english, french};
        int choice = JOptionPane.showOptionDialog(
                null,
                Messages.text("language.prompt"),
                Messages.text("language.title"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                labels,
                english);
        if (choice < ENGLISH_CHOICE || choice >= LOCALES.length) {
            return Locale.ENGLISH;
        }
        return LOCALES[choice];
    }
}
