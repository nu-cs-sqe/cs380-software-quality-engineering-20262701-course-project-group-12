package ui;

import domain.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WelcomeControllerTests {
    private static final String NAMES_REQUIRED_MESSAGE = "Both player names are required.";
    private static final String DUPLICATE_NAMES_MESSAGE = "Players must use different names.";
    private static final Runnable NO_WINDOW = () -> { };

    private WelcomeView view;

    @BeforeEach
    public void createViewMock() {
        view = mock(WelcomeView.class);
    }

    @Test
    public void WelcomeController_StartedGame_ThrowsIllegalStateException() {
        Game game = new Game();
        game.start("Alice", "Bob");

        assertThrows(IllegalStateException.class, () -> new WelcomeController(game));
    }

    @Test
    public void WelcomeController_NullGame_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WelcomeController(null));
    }

    @Test
    public void StartGame_WithMissingPlayerName_ShowsRequiredNames() {
        Game game = new Game();
        view.showNamesRequired();
        WelcomeController controller = controllerWithView(game);

        controller.startGame("", "Bob");

        verify(view);
        assertFalse(game.isStarted());
    }

    @Test
    public void StartGame_WithMissingPlayerTwoName_ShowsRequiredNames() {
        Game game = new Game();
        view.showNamesRequired();
        WelcomeController controller = controllerWithView(game);

        controller.startGame("Alice", "");

        verify(view);
        assertFalse(game.isStarted());
    }

    @Test
    public void StartGame_WithWhitespaceOnlyNames_ShowsRequiredNames() {
        Game game = new Game();
        view.showNamesRequired();
        WelcomeController controller = controllerWithView(game);

        controller.startGame("   ", "\t");

        verify(view);
        assertFalse(game.isStarted());
    }

    @Test
    public void WelcomeView_ShowNamesRequired_DisplaysRequiredNamesMessage() {
        WelcomeView welcomeView = new WelcomeView(controllerThatIsNeverCalled(), NO_WINDOW);

        welcomeView.showNamesRequired();

        assertEquals(NAMES_REQUIRED_MESSAGE, welcomeView.errorMessage());
    }

    @Test
    public void WelcomeView_ShowDuplicateNames_DisplaysDifferentNamesMessage() {
        WelcomeView welcomeView = new WelcomeView(controllerThatIsNeverCalled(), NO_WINDOW);

        welcomeView.showDuplicateNames();

        assertEquals(DUPLICATE_NAMES_MESSAGE, welcomeView.errorMessage());
    }

    @Test
    public void StartGame_WithDuplicateNames_ShowsDuplicateNames() {
        Game game = new Game();
        view.showDuplicateNames();
        WelcomeController controller = controllerWithView(game);

        controller.startGame("Alex", "Alex");

        verify(view);
        assertFalse(game.isStarted());
    }

    @Test
    public void StartGame_WithSameNameInDifferentCase_ShowsDuplicateNames() {
        Game game = new Game();
        view.showDuplicateNames();
        WelcomeController controller = controllerWithView(game);

        controller.startGame("Alice", "ALICE");

        verify(view);
        assertFalse(game.isStarted());
    }

    @Test
    public void StartGame_WithOneCharacterNames_ClosesWelcomeView() {
        Game game = new Game();
        view.close();
        view.showGame(game);
        WelcomeController controller = controllerWithView(game);

        controller.startGame("A", "B");

        verify(view);
        assertTrue(game.isStarted());
    }

    @Test
    public void StartGame_WithValidNames_ClosesWelcomeView() {
        Game game = new Game();
        view.close();
        view.showGame(game);
        WelcomeController controller = controllerWithView(game);

        controller.startGame("Alice", "Bob");

        verify(view);
        assertTrue(game.isStarted());
    }

    @Test
    public void StartGame_WithSurroundingWhitespace_PassesNamesToGame() {
        Game game = new Game();
        view.close();
        view.showGame(game);
        WelcomeController controller = controllerWithView(game);

        controller.startGame(" Alice ", "\tBob\n");

        verify(view);
        assertEquals("Alice", game.playerOneName());
        assertEquals("Bob", game.playerTwoName());
    }

    private static WelcomeController controllerThatIsNeverCalled() {
        WelcomeController controller = mock(WelcomeController.class);
        replay(controller);
        return controller;
    }

    private WelcomeController controllerWithView(Game game) {
        replay(view);
        WelcomeController controller = new WelcomeController(game);
        controller.setView(view);
        return controller;
    }
}
