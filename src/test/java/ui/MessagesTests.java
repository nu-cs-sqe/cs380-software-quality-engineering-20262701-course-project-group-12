package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessagesTests {
    private static final String WELCOME_START_KEY = "welcome.start";
    private static final String PLAYER_ONE_KEY = "stats.playerOne";
    private static final String CHECK_KEY = "stats.check";
    private static final String PLAYER_NAME = "Ada";
    private static final String START_GAME_ENGLISH = "Start Game";
    private static final String START_GAME_FRENCH = "Démarrer la partie";
    private static final String PLAYER_ONE_ENGLISH = "Player 1: " + PLAYER_NAME + " (White)";
    private static final String PLAYER_ONE_FRENCH = "Joueur 1 : " + PLAYER_NAME + " (Blancs)";
    private static final String CHECK_ENGLISH = "Current player: " + PLAYER_NAME + " (in check)";
    private static final String CHECK_FRENCH = PLAYER_NAME + " est en échec";

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

    @Test
    public void format_EnglishLocale_FillsNamePlaceholder() {
        Messages.useLocale(Locale.ENGLISH);

        String text = Messages.format(PLAYER_ONE_KEY, PLAYER_NAME);

        assertEquals(PLAYER_ONE_ENGLISH, text);
    }

    @Test
    public void format_FrenchLocale_FillsNamePlaceholder() {
        Messages.useLocale(Locale.FRENCH);

        String text = Messages.format(PLAYER_ONE_KEY, PLAYER_NAME);

        assertEquals(PLAYER_ONE_FRENCH, text);
    }

    @Test
    public void format_EnglishLocale_KeepsNameAfterLabel() {
        Messages.useLocale(Locale.ENGLISH);

        String text = Messages.format(CHECK_KEY, PLAYER_NAME);

        assertEquals(CHECK_ENGLISH, text);
    }

    @Test
    public void format_FrenchLocale_PutsNameFirst() {
        Messages.useLocale(Locale.FRENCH);

        String text = Messages.format(CHECK_KEY, PLAYER_NAME);

        assertEquals(CHECK_FRENCH, text);
    }
}
