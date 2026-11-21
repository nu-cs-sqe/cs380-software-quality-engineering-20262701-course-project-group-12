package ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessagesTests {
    private static final String WELCOME_START_KEY = "welcome.start";
    private static final String START_GAME_ENGLISH = "Start Game";

    @Test
    public void text_NoLocaleChosen_UsesEnglish() {
        String text = Messages.text(WELCOME_START_KEY);

        assertEquals(START_GAME_ENGLISH, text);
    }
}
