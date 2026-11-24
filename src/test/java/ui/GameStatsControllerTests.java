package ui;

import domain.Game;
import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import java.time.Duration;
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
        expectStartingClocks(game);
        replay(game);

        assertDoesNotThrow(() -> new GameStatsController(game));
    }

    @Test
    void GameStatsController_UnstartedGame_ThrowsIllegalArgumentException() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(false);
        expectStartingClocks(game);
        replay(game);

        assertThrows(IllegalArgumentException.class, () -> new GameStatsController(game));
    }

    @Test
    void CreateView_PlayerOneToMove_ShowsSetupLines() {
        Game game = startedGame("Ada", "Bo", "Ada", PieceColor.WHITE);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Ada", view.whiteName());
        assertEquals("Bo", view.blackName());
        assertEquals("Current player: Ada", view.statusLine());
    }

    @Test
    void CreateView_StartedGame_ShowsBothClocksAtTenMinutes() {
        Game game = startedGame("Ada", "Bo", "Ada", PieceColor.WHITE);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("10:00", view.remainingTime(PieceColor.WHITE));
        assertEquals("10:00", view.remainingTime(PieceColor.BLACK));
    }

    @Test
    void RefreshStatus_WhiteTimeReduced_ShowsUpdatedWhiteClock() {
        Game game = createMock(Game.class);
        expect(game.isStarted()).andStubReturn(true);
        expect(game.playerOneName()).andStubReturn("Ada");
        expect(game.playerTwoName()).andStubReturn("Bo");
        expect(game.playerToMoveName()).andStubReturn("Ada");
        expect(game.colorToMove()).andStubReturn(PieceColor.WHITE);
        expect(game.isOver()).andStubReturn(false);
        expect(game.isPlayerToMoveInCheck()).andStubReturn(false);
        expect(game.remainingTime(PieceColor.WHITE))
                .andReturn(Duration.ofMinutes(10))
                .andReturn(Duration.ofMinutes(9).plusSeconds(59));
        expect(game.remainingTime(PieceColor.BLACK)).andStubReturn(Duration.ofMinutes(10));
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("9:59", view.remainingTime(PieceColor.WHITE));
        assertEquals("10:00", view.remainingTime(PieceColor.BLACK));
    }

    @Test
    void CreateView_OneCharacterNames_ShowsSetupLines() {
        Game game = startedGame("A", "B", "A", PieceColor.WHITE);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("A", view.whiteName());
        assertEquals("B", view.blackName());
        assertEquals("Current player: A", view.statusLine());
    }

    @Test
    void CreateView_NamesWithSpaces_ShowsSetupLines() {
        Game game = startedGame("Ann Marie", "Li Wei", "Ann Marie", PieceColor.WHITE);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Ann Marie", view.whiteName());
        assertEquals("Li Wei", view.blackName());
        assertEquals("Current player: Ann Marie", view.statusLine());
    }

    @Test
    void CreateView_SimilarButDifferentNames_ShowsBothPlayers() {
        Game game = startedGame("Alex", "Alexa", "Alex", PieceColor.WHITE);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Alex", view.whiteName());
        assertEquals("Alexa", view.blackName());
        assertEquals("Current player: Alex", view.statusLine());
    }

    @Test
    void CreateView_PlayerTwoToMove_ShowsPlayerTwoAsCurrent() {
        Game game = startedGame("Ada", "Bo", "Bo", PieceColor.BLACK);

        GameStatsView view = new GameStatsController(game).createView();

        assertEquals("Ada", view.whiteName());
        assertEquals("Bo", view.blackName());
        assertEquals("Current player: Bo", view.statusLine());
    }

    @Test
    void RefreshStatus_AfterTurn_ShowsPlayerTwo() {
        Game game = gameAfterTurnToBlack(false);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Current player: Bo", view.statusLine());
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
        expect(game.isDrawnOnTime()).andStubReturn(false);
        expect(game.isWonOnTime()).andStubReturn(false);
        expect(game.isWonByResignation()).andStubReturn(false);
        expect(game.winnerName()).andStubReturn("Bo");
        expect(game.winnerColor()).andStubReturn(PieceColor.BLACK);
        expectStartingClocks(game);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Checkmate: Bo (Black) wins", view.statusLine());
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
        expect(game.isDrawnOnTime()).andStubReturn(false);
        expect(game.isWonOnTime()).andStubReturn(false);
        expect(game.isWonByResignation()).andStubReturn(false);
        expect(game.winnerName()).andStubReturn("Ada");
        expect(game.winnerColor()).andStubReturn(PieceColor.WHITE);
        expectStartingClocks(game);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Checkmate: Ada (White) wins", view.statusLine());
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
        expectStartingClocks(game);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by repetition", view.statusLine());
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
        expectStartingClocks(game);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by stalemate", view.statusLine());
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
        expectStartingClocks(game);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by agreement", view.statusLine());
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
        expectStartingClocks(game);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by the fifty-move rule", view.statusLine());
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
        expectStartingClocks(game);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by the seventy-five-move rule", view.statusLine());
    }

    @Test
    public void RefreshStatus_PlayerToMoveInCheck_ShowsTheCheck() {
        Game game = gameAfterTurnToBlack(true);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Current player: Bo (in check)", view.statusLine());
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
        expect(game.isDrawnOnTime()).andStubReturn(false);
        expect(game.isWonOnTime()).andStubReturn(false);
        expect(game.isWonByResignation()).andStubReturn(false);
        expect(game.winnerName()).andStubReturn("Bo");
        expect(game.winnerColor()).andStubReturn(PieceColor.BLACK);
        expectStartingClocks(game);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Checkmate: Bo (Black) wins", view.statusLine());
    }

    @Test
    void RefreshStatus_PlayerOneResigns_ShowsPlayerTwoWins() {
        Game game = gameWonByResignation("Bo", PieceColor.BLACK);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Resignation: Bo (Black) wins", view.statusLine());
    }

    @Test
    void RefreshStatus_PlayerTwoResigns_ShowsPlayerOneWins() {
        Game game = gameWonByResignation("Ada", PieceColor.WHITE);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Resignation: Ada (White) wins", view.statusLine());
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
        expectStartingClocks(game);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw by dead position", view.statusLine());
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
        expect(game.isDrawnOnTime()).andStubReturn(false);
        expect(game.isWonOnTime()).andStubReturn(false);
        expect(game.isWonByResignation()).andStubReturn(false);
        expect(game.winnerName()).andStubReturn("Bo");
        expect(game.winnerColor()).andStubReturn(PieceColor.BLACK);
        expectStartingClocks(game);
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
        expectStartingClocks(game);
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
        expectStartingClocks(game);
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
        expectStartingClocks(game);
        replay(game);
        return game;
    }

    @Test
    void RefreshStatus_PlayerOneLosesOnTime_ShowsPlayerTwoWinsOnTime() {
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
        expect(game.isDrawnOnTime()).andStubReturn(false);
        expect(game.isWonOnTime()).andStubReturn(true);
        expect(game.winnerName()).andStubReturn("Bo");
        expect(game.winnerColor()).andStubReturn(PieceColor.BLACK);
        expectStartingClocks(game);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("On time: Bo (Black) wins", view.statusLine());
    }

    @Test
    void RefreshStatus_PlayerTwoLosesOnTime_ShowsPlayerOneWinsOnTime() {
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
        expect(game.isDrawnOnTime()).andStubReturn(false);
        expect(game.isWonOnTime()).andStubReturn(true);
        expect(game.winnerName()).andStubReturn("Ada");
        expect(game.winnerColor()).andStubReturn(PieceColor.WHITE);
        expectStartingClocks(game);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("On time: Ada (White) wins", view.statusLine());
    }

    @Test
    void RefreshStatus_DrawOnTime_ShowsTheDraw() {
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
        expect(game.isDrawnOnTime()).andStubReturn(true);
        expectStartingClocks(game);
        replay(game);
        GameStatsController controller = new GameStatsController(game);
        GameStatsView view = controller.createView();

        controller.refreshStatus();

        assertEquals("Draw on time", view.statusLine());
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
        expect(game.isDrawnOnTime()).andStubReturn(false);
        expect(game.isWonOnTime()).andStubReturn(false);
        expect(game.isWonByResignation()).andStubReturn(true);
        expect(game.winnerName()).andStubReturn(winnerName);
        expect(game.winnerColor()).andStubReturn(winnerColor);
        expectStartingClocks(game);
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
        expectStartingClocks(game);
        replay(game);
        return game;
    }

    private static void expectStartingClocks(Game game) {
        expect(game.remainingTime(PieceColor.WHITE)).andStubReturn(Duration.ofMinutes(10));
        expect(game.remainingTime(PieceColor.BLACK)).andStubReturn(Duration.ofMinutes(10));
    }
}
