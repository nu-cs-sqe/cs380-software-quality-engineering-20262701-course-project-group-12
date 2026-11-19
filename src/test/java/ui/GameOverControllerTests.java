package ui;

import domain.Game;
import domain.piece.PieceColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class GameOverControllerTests {
    private Game game;
    private GameOverView view;

    @BeforeEach
    void createMocks() {
        game = createNiceMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        view = mock(GameOverView.class);
    }

    @Test
    void ShowIfOver_GameInProgress_OpensNoDialog() {
        expect(game.isOver()).andStubReturn(false);
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }

    @Test
    void ShowIfOver_WhiteWinsByCheckmate_ShowsTheWinner() {
        gameWonBy("Ada", PieceColor.WHITE);
        expect(view.askNextStep("Checkmate: Ada (White) wins"))
                .andReturn(GameOverChoice.KEEP_VIEWING);
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }

    @Test
    void ShowIfOver_BlackWinsByCheckmate_ShowsTheWinner() {
        gameWonBy("Bo", PieceColor.BLACK);
        expect(view.askNextStep("Checkmate: Bo (Black) wins"))
                .andReturn(GameOverChoice.KEEP_VIEWING);
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }


    @Test
    void ShowIfOver_WhiteWinsByResignation_ShowsTheWinner() {
        gameWonBy("Ada", PieceColor.WHITE);
        expect(game.isWonByResignation()).andStubReturn(true);
        expect(view.askNextStep("Resignation: Ada (White) wins"))
                .andReturn(GameOverChoice.KEEP_VIEWING);
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }


    @Test
    void ShowIfOver_BlackWinsByResignation_ShowsTheWinner() {
        gameWonBy("Bo", PieceColor.BLACK);
        expect(game.isWonByResignation()).andStubReturn(true);
        expect(view.askNextStep("Resignation: Bo (Black) wins"))
                .andReturn(GameOverChoice.KEEP_VIEWING);
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }


    @Test
    void ShowIfOver_DrawByStalemate_ShowsTheDraw() {
        expect(game.isOver()).andStubReturn(true);
        expect(game.isStalemate()).andStubReturn(true);
        expect(view.askNextStep("Draw by stalemate")).andReturn(GameOverChoice.KEEP_VIEWING);
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }


    @Test
    void ShowIfOver_DrawByDeadPosition_ShowsTheDraw() {
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByDeadPosition()).andStubReturn(true);
        expect(view.askNextStep("Draw by dead position")).andReturn(GameOverChoice.KEEP_VIEWING);
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }


    @Test
    void ShowIfOver_DrawByRepetition_ShowsTheDraw() {
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(true);
        expect(view.askNextStep("Draw by repetition")).andReturn(GameOverChoice.KEEP_VIEWING);
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }


    @Test
    void ShowIfOver_DrawByFiftyMove_ShowsTheDraw() {
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByFiftyMove()).andStubReturn(true);
        expect(view.askNextStep("Draw by the fifty-move rule")).andReturn(GameOverChoice.KEEP_VIEWING);
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }


    @Test
    void ShowIfOver_DrawBySeventyFiveMove_ShowsTheDraw() {
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnBySeventyFiveMove()).andStubReturn(true);
        expect(view.askNextStep("Draw by the seventy-five-move rule")).andReturn(GameOverChoice.KEEP_VIEWING);
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }


    @Test
    void ShowIfOver_DrawByAgreement_ShowsTheDraw() {
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByAgreement()).andStubReturn(true);
        expect(view.askNextStep("Draw by agreement")).andReturn(GameOverChoice.KEEP_VIEWING);
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }


    @Test
    void ShowIfOver_DialogAlreadyOpened_OpensNoSecondDialog() {
        expect(game.isOver()).andStubReturn(true);
        expect(game.isStalemate()).andStubReturn(true);
        expect(view.askNextStep("Draw by stalemate")).andReturn(GameOverChoice.KEEP_VIEWING);
        GameOverController controller = controller();
        controller.showIfOver();

        controller.showIfOver();

        verify(view);
    }


    @Test
    void ShowIfOver_PlayersChooseNewGame_ReturnsToWelcomeWithNames() {
        expect(game.isOver()).andStubReturn(true);
        expect(game.isStalemate()).andStubReturn(true);
        expect(view.askNextStep("Draw by stalemate")).andReturn(GameOverChoice.NEW_GAME);
        view.returnToWelcome("Ada", "Bo");
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }


    @Test
    void ShowIfOver_PlayersChooseClose_ExitsTheApplication() {
        expect(game.isOver()).andStubReturn(true);
        expect(game.isStalemate()).andStubReturn(true);
        expect(view.askNextStep("Draw by stalemate")).andReturn(GameOverChoice.CLOSE);
        view.exitApplication();
        GameOverController controller = controller();

        controller.showIfOver();

        verify(view);
    }


    private void gameWonBy(String winnerName, PieceColor winnerColor) {
        expect(game.isOver()).andStubReturn(true);
        expect(game.winnerName()).andStubReturn(winnerName);
        expect(game.winnerColor()).andStubReturn(winnerColor);
    }

    private GameOverController controller() {
        replay(game, view);
        return new GameOverController(game, new GameStatsController(game), view);
    }
}
