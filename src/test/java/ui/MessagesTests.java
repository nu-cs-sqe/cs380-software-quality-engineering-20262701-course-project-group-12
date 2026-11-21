package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessagesTests {
    private static final String WELCOME_START_KEY = "welcome.start";
    private static final String START_GAME_ENGLISH = "Start Game";
    private static final String START_GAME_FRENCH = "Démarrer la partie";

    @AfterEach
    public void restoreEnglish() {
        Messages.useLocale(Locale.ENGLISH);
    }

    @Test
    public void text_NoLocaleChosen_UsesEnglish() {
        String text = Messages.text(WELCOME_START_KEY);

        assertEquals(START_GAME_ENGLISH, text);
    }

    @Test
    public void text_EnglishLocale_UsesEnglish() {
        Messages.useLocale(Locale.ENGLISH);

        String text = Messages.text(WELCOME_START_KEY);

        assertEquals(START_GAME_ENGLISH, text);
    }

    @Test
    public void text_FrenchLocale_UsesFrench() {
        Messages.useLocale(Locale.FRENCH);

        String text = Messages.text(WELCOME_START_KEY);

        assertEquals(START_GAME_FRENCH, text);
    }

    @Test
    public void text_FranceLocale_UsesFrench() {
        Messages.useLocale(Locale.FRANCE);

        String text = Messages.text(WELCOME_START_KEY);

        assertEquals(START_GAME_FRENCH, text);
    }
}
