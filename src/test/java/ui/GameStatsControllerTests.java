package ui;

import domain.Game;
import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameStatsControllerTests {

    @Test
    void GameStatsController_StartedGame_IsCreated() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        replay(game);

        assertDoesNotThrow(() -> new GameStatsController(game));
    }

    @Test
    void GameStatsController_UnstartedGame_ThrowsIllegalArgumentException() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(false);
        replay(game);

        assertThrows(IllegalArgumentException.class, () -> new GameStatsController(game));
    }

    @Test
    void CreateView_PlayerOneToMove_ShowsSetupLines() {
        Game game = startedGame("Ada", "Bo", "Ada", PieceColor.WHITE);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Ada", view.whiteName());
        assertEquals("Bo", view.blackName());
        assertEquals("Current player: Ada", view.currentPlayerLine());
    }

    @Test
    void CreateView_OneCharacterNames_ShowsSetupLines() {
        Game game = startedGame("A", "B", "A", PieceColor.WHITE);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("A", view.whiteName());
        assertEquals("B", view.blackName());
        assertEquals("Current player: A", view.currentPlayerLine());
    }

    @Test
    void CreateView_NamesWithSpaces_ShowsSetupLines() {
        Game game = startedGame("Ann Marie", "Li Wei", "Ann Marie", PieceColor.WHITE);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Ann Marie", view.whiteName());
        assertEquals("Li Wei", view.blackName());
        assertEquals("Current player: Ann Marie", view.currentPlayerLine());
    }

    @Test
    void CreateView_SimilarButDifferentNames_ShowsBothPlayers() {
        Game game = startedGame("Alex", "Alexa", "Alex", PieceColor.WHITE);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Alex", view.whiteName());
        assertEquals("Alexa", view.blackName());
        assertEquals("Current player: Alex", view.currentPlayerLine());
    }

    @Test
    void CreateView_PlayerTwoToMove_ShowsPlayerTwoAsCurrent() {
        Game game = startedGame("Ada", "Bo", "Bo", PieceColor.BLACK);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Ada", view.whiteName());
        assertEquals("Bo", view.blackName());
        assertEquals("Current player: Bo", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_AfterTurn_ShowsPlayerTwo() {
        Game game = gameAfterTurnToBlack(false);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Current player: Bo", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_PlayerTwoCheckmatesPlayerOne_ShowsPlayerTwoWins() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.isDrawnByAgreement()).andStubReturn(false);
        expect(game.isStalemate()).andStubReturn(false);
        expect(game.isDrawnByDeadPosition()).andStubReturn(false);
        expect(game.isDrawnByFiftyMove()).andStubReturn(false);
        expect(game.isDrawnBySeventyFiveMove()).andStubReturn(false);
        expect(game.isWonByResignation()).andStubReturn(false);
        expect(game.winnerName()).andStubReturn("Bo");
        expect(game.winnerColor()).andStubReturn(PieceColor.BLACK);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Checkmate: Bo (Black) wins", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_PlayerOneCheckmatesPlayerTwo_ShowsPlayerOneWins() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.isDrawnByAgreement()).andStubReturn(false);
        expect(game.isStalemate()).andStubReturn(false);
        expect(game.isDrawnByDeadPosition()).andStubReturn(false);
        expect(game.isDrawnByFiftyMove()).andStubReturn(false);
        expect(game.isDrawnBySeventyFiveMove()).andStubReturn(false);
        expect(game.isWonByResignation()).andStubReturn(false);
        expect(game.winnerName()).andStubReturn("Ada");
        expect(game.winnerColor()).andStubReturn(PieceColor.WHITE);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Checkmate: Ada (White) wins", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_DrawByRepetition_ShowsTheDraw() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(true);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by repetition", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_DrawByStalemate_ShowsTheDraw() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.isDrawnByAgreement()).andStubReturn(false);
        expect(game.isStalemate()).andStubReturn(true);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by stalemate", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_DrawByAgreement_ShowsTheDraw() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.isDrawnByAgreement()).andStubReturn(true);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by agreement", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_DrawByFiftyMove_ShowsTheDraw() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.isDrawnByAgreement()).andStubReturn(false);
        expect(game.isStalemate()).andStubReturn(false);
        expect(game.isDrawnByDeadPosition()).andStubReturn(false);
        expect(game.isDrawnByFiftyMove()).andStubReturn(true);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by the fifty-move rule", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_DrawBySeventyFiveMove_ShowsTheDraw() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.isDrawnByAgreement()).andStubReturn(false);
        expect(game.isStalemate()).andStubReturn(false);
        expect(game.isDrawnByDeadPosition()).andStubReturn(false);
        expect(game.isDrawnByFiftyMove()).andStubReturn(false);
        expect(game.isDrawnBySeventyFiveMove()).andStubReturn(true);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by the seventy-five-move rule", view.currentPlayerLine());
    }

    @Test
    public void RefreshStatus_PlayerToMoveInCheck_ShowsTheCheck() {
        Game game = gameAfterTurnToBlack(true);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Current player: Bo (in check)", view.currentPlayerLine());
    }

    @Test
    public void RefreshStatus_CheckmatedPlayerInCheck_ShowsTheCheckmate() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isPlayerToMoveInCheck()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.isDrawnByAgreement()).andStubReturn(false);
        expect(game.isStalemate()).andStubReturn(false);
        expect(game.isDrawnByDeadPosition()).andStubReturn(false);
        expect(game.isDrawnByFiftyMove()).andStubReturn(false);
        expect(game.isDrawnBySeventyFiveMove()).andStubReturn(false);
        expect(game.isWonByResignation()).andStubReturn(false);
        expect(game.winnerName()).andStubReturn("Bo");
        expect(game.winnerColor()).andStubReturn(PieceColor.BLACK);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Checkmate: Bo (Black) wins", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_PlayerOneResigns_ShowsPlayerTwoWins() {
        Game game = gameWonByResignation("Bo", PieceColor.BLACK);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Resignation: Bo (Black) wins", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_PlayerTwoResigns_ShowsPlayerOneWins() {
        Game game = gameWonByResignation("Ada", PieceColor.WHITE);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Resignation: Ada (White) wins", view.currentPlayerLine());
    }

    @Test
    void RefreshStatus_DrawByDeadPosition_ShowsTheDraw() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.isDrawnByAgreement()).andStubReturn(false);
        expect(game.isStalemate()).andStubReturn(false);
        expect(game.isDrawnByDeadPosition()).andStubReturn(true);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by dead position", view.currentPlayerLine());
    }

    @Test
    void CreateView_WhiteToMove_HighlightsWhite() {
        Game game = startedGame("Ada", "Bo", "Ada", PieceColor.WHITE);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals(Optional.of(PieceColor.WHITE), view.highlightedColor());
    }

    @Test
    void RefreshStatus_AfterTurn_HighlightsBlack() {
        Game game = gameAfterTurnToBlack(false);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals(Optional.of(PieceColor.BLACK), view.highlightedColor());
    }

    @Test
    void RefreshStatus_GameOver_HighlightsNoPlayer() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.colorToMove()).andStubReturn(PieceColor.WHITE);
        expect(game.isPlayerToMoveInCheck()).andStubReturn(false);
        expect(game.isOver()).andReturn(false).andReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.isDrawnByAgreement()).andStubReturn(false);
        expect(game.isStalemate()).andStubReturn(false);
        expect(game.isDrawnByDeadPosition()).andStubReturn(false);
        expect(game.isDrawnByFiftyMove()).andStubReturn(false);
        expect(game.isDrawnBySeventyFiveMove()).andStubReturn(false);
        expect(game.isWonByResignation()).andStubReturn(false);
        expect(game.winnerName()).andStubReturn("Bo");
        expect(game.winnerColor()).andStubReturn(PieceColor.BLACK);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals(Optional.empty(), view.highlightedColor());
    }

    @Test
    void CreateView_NotInCheck_ShowsTurnTone() {
        Game game = startedGame("Ada", "Bo", "Ada", PieceColor.WHITE);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals(GameStatsView.StatusTone.TURN, view.statusTone());
    }

    @Test
    void RefreshStatus_PlayerToMoveInCheck_ShowsCheckToneAndHighlightsBlack() {
        Game game = gameAfterTurnToBlack(true);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals(GameStatsView.StatusTone.CHECK, view.statusTone());
        assertEquals(Optional.of(PieceColor.BLACK), view.highlightedColor());
    }

    @Test
    void RefreshStatus_DrawByRepetition_ShowsResultTone() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(true);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals(GameStatsView.StatusTone.RESULT, view.statusTone());
    }

    @Test
    void RefreshStatus_AfterCheckIsLifted_ShowsTurnTone() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andReturn("Ada").andReturn("Bo");
        expect(game.colorToMove()).andReturn(PieceColor.WHITE).andReturn(PieceColor.BLACK);
        expect(game.isOver()).andStubReturn(false);
        expect(game.isPlayerToMoveInCheck()).andReturn(true).andReturn(false);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals(GameStatsView.StatusTone.TURN, view.statusTone());
    }

    private static Game gameAfterTurnToBlack(boolean blackInCheck) {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andReturn("Ada").andReturn("Bo");
        expect(game.colorToMove()).andReturn(PieceColor.WHITE).andReturn(PieceColor.BLACK);
        expect(game.isOver()).andStubReturn(false);
        expect(game.isPlayerToMoveInCheck()).andStubReturn(blackInCheck);
        replay(game);
        return game;
    }

    private static Game gameWonByResignation(String winnerName, PieceColor winnerColor) {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.isOver()).andStubReturn(true);
        expect(game.isDrawnByRepetition()).andStubReturn(false);
        expect(game.isDrawnByAgreement()).andStubReturn(false);
        expect(game.isStalemate()).andStubReturn(false);
        expect(game.isDrawnByDeadPosition()).andStubReturn(false);
        expect(game.isDrawnByFiftyMove()).andStubReturn(false);
        expect(game.isDrawnBySeventyFiveMove()).andStubReturn(false);
        expect(game.isWonByResignation()).andStubReturn(true);
        expect(game.winnerName()).andStubReturn(winnerName);
        expect(game.winnerColor()).andStubReturn(winnerColor);
        replay(game);
        return game;
    }

    private static Game startedGame(String playerOneName, String playerTwoName,
            String playerToMoveName, PieceColor colorToMove) {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn(playerOneName);
        expect(game.playerTwoName()).andStubReturn(playerTwoName);
        expect(game.playerToMoveName()).andStubReturn(playerToMoveName);
        expect(game.colorToMove()).andStubReturn(colorToMove);
        expect(game.isOver()).andStubReturn(false);
        expect(game.isPlayerToMoveInCheck()).andStubReturn(false);
        replay(game);
        return game;
    }
}
