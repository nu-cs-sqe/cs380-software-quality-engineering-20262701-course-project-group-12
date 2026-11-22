package domain;

import domain.piece.PieceColor;
import domain.piece.PieceType;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location A2 = new Location(0, 2);
    private static final Location A8 = new Location(0, 8);
    private static final Location B8 = new Location(1, 8);
    private static final Location C6 = new Location(2, 6);
    private static final Location D1 = new Location(3, 1);
    private static final Location D8 = new Location(3, 8);
    private static final Location E1 = new Location(4, 1);
    private static final Location E2 = new Location(4, 2);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location E7 = new Location(4, 7);
    private static final Location E8 = new Location(4, 8);
    private static final Location F2 = new Location(5, 2);
    private static final Location F3 = new Location(5, 3);
    private static final Location F6 = new Location(5, 6);
    private static final Location F7 = new Location(5, 7);
    private static final Location G1 = new Location(6, 1);
    private static final Location G2 = new Location(6, 2);
    private static final Location G4 = new Location(6, 4);
    private static final Location G8 = new Location(6, 8);
    private static final Location H4 = new Location(7, 4);
    private static final Location H5 = new Location(7, 5);
    private static final int ROUND_TRIPS_FOR_SECOND_OCCURRENCE = 1;
    private static final int ROUND_TRIPS_FOR_THIRD_OCCURRENCE = 2;
    private static final int ROUND_TRIPS_FOR_FOURTH_OCCURRENCE = 3;
    private static final int ROUND_TRIPS_FOR_FIFTH_OCCURRENCE = 4;
    private static final int ROUND_TRIPS_FOR_NINETY_NINE_HALF_MOVES = 24;
    private static final int ROUND_TRIPS_FOR_ONE_HUNDRED_HALF_MOVES = 25;
    private static final int ROUND_TRIPS_FOR_ONE_HUNDRED_FORTY_NINE_HALF_MOVES = 37;
    private static final int MOVES_BY_BOTH_PLAYERS = 2;
    private static final Instant CLOCK_START = Instant.parse("2026-01-01T00:00:00Z");
    private static final Duration ONE_MILLISECOND = Duration.ofMillis(1);

    @Test
    public void IsStarted_NewGame_ReturnsFalse() {
        Game game = new Game();

        assertFalse(game.isStarted());
    }

    @Test
    public void start_EmptyPlayerOneName_ThrowsMissingPlayerNameException() {
        Game game = new Game();

        assertThrows(MissingPlayerNameException.class, () -> game.start("", "Bob"));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_BothNamesEmpty_ThrowsMissingPlayerNameException() {
        Game game = new Game();

        assertThrows(MissingPlayerNameException.class, () -> game.start("", ""));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_EmptyPlayerTwoName_ThrowsMissingPlayerNameException() {
        Game game = new Game();

        assertThrows(MissingPlayerNameException.class, () -> game.start("Alice", ""));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_WhitespaceOnlyPlayerOneName_ThrowsMissingPlayerNameException() {
        Game game = new Game();

        assertThrows(MissingPlayerNameException.class, () -> game.start("   ", "Bob"));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_WhitespaceOnlyPlayerTwoName_ThrowsMissingPlayerNameException() {
        Game game = new Game();

        assertThrows(MissingPlayerNameException.class, () -> game.start("Alice", "   "));
        assertFalse(game.isStarted());
    }

    @Test
    public void Start_WithTwoValidNames_StartsGame() {
        Game game = new Game();

        game.start("Alice", "Bob");

        assertTrue(game.isStarted());
    }

    @Test
    public void Start_WithOneCharacterNames_StartsGame() {
        Game game = new Game();

        game.start("A", "B");

        assertTrue(game.isStarted());
    }

    @Test
    public void Accessors_UnstartedGame_ThrowIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::playerOne);
        assertThrows(IllegalStateException.class, game::playerTwo);
        assertThrows(IllegalStateException.class, game::playerToMove);
        assertThrows(IllegalStateException.class, game::board);
    }

    @Test
    public void Accessors_StartedGame_ReturnSetupState() {
        Game game = new Game();

        game.start("Alice", "Bob");

        assertEquals("Alice", game.playerOne().getName());
        assertEquals("Bob", game.playerTwo().getName());
        assertEquals(game.playerOne(), game.playerToMove());
        assertTrue(game.board().isOccupied(A1));
    }

    @Test
    public void Start_WithValidNames_CreatesWhitePlayerOne() {
        Game game = new Game();

        game.start(" Alice ", "Bob");

        assertEquals("Alice", game.playerOne().getName());
        assertEquals(PieceColor.WHITE, game.playerOne().getColor());
    }

    @Test
    public void Start_WithValidNames_CreatesBlackPlayerTwo() {
        Game game = new Game();

        game.start("Alice", " Bob ");

        assertEquals("Bob", game.playerTwo().getName());
        assertEquals(PieceColor.BLACK, game.playerTwo().getColor());
    }

    @Test
    public void Start_WithValidNames_SetsPlayerOneToMove() {
        Game game = new Game();

        game.start("Alice", "Bob");

        assertEquals(game.playerOne(), game.playerToMove());
    }

    @Test
    public void Start_WithValidNames_CreatesBoard() {
        Game game = new Game();

        game.start("Alice", "Bob");

        assertTrue(game.board().isOccupied(A1));
    }

    @Test
    public void start_DuplicateNames_ThrowsDuplicatePlayerNameException() {
        Game game = new Game();

        assertThrows(DuplicatePlayerNameException.class, () -> game.start("Alex", "Alex"));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_SameNameDifferentCase_ThrowsDuplicatePlayerNameException() {
        Game game = new Game();

        assertThrows(DuplicatePlayerNameException.class, () -> game.start("Alice", "ALICE"));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_SameNameAfterTrim_ThrowsDuplicatePlayerNameException() {
        Game game = new Game();

        assertThrows(DuplicatePlayerNameException.class, () -> game.start(" Alice ", "Alice"));
        assertFalse(game.isStarted());
    }

    @Test
    public void start_MixedCaseNames_KeepsEnteredCase() {
        Game game = new Game();

        game.start("ALICE", "bob");

        assertEquals("ALICE", game.playerOne().getName());
        assertEquals("bob", game.playerTwo().getName());
    }

    @Test
    public void PlayerOneName_StartedGame_ReturnsPlayerOneName() {
        Game game = new Game();
        game.start("Alice", "Bob");

        String name = game.playerOneName();

        assertEquals("Alice", name);
    }

    @Test
    public void PlayerOneName_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::playerOneName);
    }

    @Test
    public void PlayerTwoName_StartedGame_ReturnsPlayerTwoName() {
        Game game = new Game();
        game.start("Alice", "Bob");

        String name = game.playerTwoName();

        assertEquals("Bob", name);
    }

    @Test
    public void PlayerTwoName_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::playerTwoName);
    }

    @Test
    public void PlayerToMoveName_StartedGame_ReturnsPlayerOneName() {
        Game game = new Game();
        game.start("Alice", "Bob");

        String name = game.playerToMoveName();

        assertEquals("Alice", name);
    }

    @Test
    public void PlayerToMoveName_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::playerToMoveName);
    }

    @Test
    public void ColorToMove_StartedGame_ReturnsWhite() {
        Game game = new Game();
        game.start("Alice", "Bob");

        PieceColor color = game.colorToMove();

        assertEquals(PieceColor.WHITE, color);
    }

    @Test
    public void PassTurn_AfterStart_MakesPlayerTwoToMove() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.passTurn();

        assertSame(game.playerTwo(), game.playerToMove());
        assertEquals("Bob", game.playerToMoveName());
        assertEquals(PieceColor.BLACK, game.colorToMove());
    }

    @Test
    public void PassTurn_Twice_MakesPlayerOneToMove() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.passTurn();
        game.passTurn();

        assertSame(game.playerOne(), game.playerToMove());
        assertEquals(PieceColor.WHITE, game.colorToMove());
    }

    @Test
    public void PassTurn_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::passTurn);
    }

    @Test
    public void ColorToMove_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::colorToMove);
    }

    @Test
    public void IsOver_StartedGame_ReturnsFalse() {
        Game game = new Game();
        game.start("Alice", "Bob");

        boolean over = game.isOver();

        assertFalse(over);
    }

    @Test
    public void EndByCheckmate_StartedGame_EndsTheGame() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.endByCheckmate();

        assertTrue(game.isOver());
    }

    @Test
    public void EndByCheckmate_PlayerOneToMove_MakesPlayerTwoTheWinner() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.endByCheckmate();

        assertEquals("Bob", game.winnerName());
        assertEquals(PieceColor.BLACK, game.winnerColor());
    }

    @Test
    public void EndByCheckmate_PlayerTwoToMove_MakesPlayerOneTheWinner() {
        Game game = new Game();
        game.start("Alice", "Bob");
        game.passTurn();

        game.endByCheckmate();

        assertEquals("Alice", game.winnerName());
        assertEquals(PieceColor.WHITE, game.winnerColor());
    }

    @Test
    public void WinnerName_GameNotOver_ThrowsIllegalStateException() {
        Game game = new Game();
        game.start("Alice", "Bob");

        assertThrows(IllegalStateException.class, game::winnerName);
    }

    @Test
    public void WinnerColor_GameNotOver_ThrowsIllegalStateException() {
        Game game = new Game();
        game.start("Alice", "Bob");

        assertThrows(IllegalStateException.class, game::winnerColor);
    }

    @Test
    public void EndByCheckmate_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::endByCheckmate);
    }

    @Test
    public void IsStalemate_StartedGame_ReturnsFalse() {
        Game game = new Game();
        game.start("Alice", "Bob");

        boolean stalemate = game.isStalemate();

        assertFalse(stalemate);
    }

    @Test
    public void EndByStalemate_StartedGame_EndsTheGame() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.endByStalemate();

        assertTrue(game.isOver());
        assertTrue(game.isStalemate());
    }

    @Test
    public void EndByCheckmate_StartedGame_IsNotStalemate() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.endByCheckmate();

        assertTrue(game.isOver());
        assertFalse(game.isStalemate());
    }

    @Test
    public void WinnerName_AfterStalemate_ThrowsIllegalStateException() {
        Game game = new Game();
        game.start("Alice", "Bob");
        game.endByStalemate();

        assertThrows(IllegalStateException.class, game::winnerName);
    }

    @Test
    public void WinnerColor_AfterStalemate_ThrowsIllegalStateException() {
        Game game = new Game();
        game.start("Alice", "Bob");
        game.endByStalemate();

        assertThrows(IllegalStateException.class, game::winnerColor);
    }

    @Test
    public void EndByStalemate_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::endByStalemate);
    }

    @Test
    public void CanClaimDrawByRepetition_NewGame_ReturnsFalse() {
        Game game = new Game();
        game.start("Alice", "Bob");

        boolean canClaim = game.canClaimDrawByRepetition();

        assertFalse(canClaim);
    }

    @Test
    public void CanClaimDrawByRepetition_PositionSeenTwice_ReturnsFalse() {
        Game game = gameAfterKnightRoundTrips(ROUND_TRIPS_FOR_SECOND_OCCURRENCE);

        boolean canClaim = game.canClaimDrawByRepetition();

        assertFalse(canClaim);
    }

    @Test
    public void CanClaimDrawByRepetition_PositionSeenThreeTimes_ReturnsTrue() {
        Game game = gameAfterKnightRoundTrips(ROUND_TRIPS_FOR_THIRD_OCCURRENCE);

        boolean canClaim = game.canClaimDrawByRepetition();

        assertTrue(canClaim);
    }

    @Test
    public void CanClaimDrawByRepetition_NewPositionAfterRepetition_ReturnsFalse() {
        Game game = gameAfterKnightRoundTrips(ROUND_TRIPS_FOR_THIRD_OCCURRENCE);
        playMove(game, E2, E4);

        boolean canClaim = game.canClaimDrawByRepetition();

        assertFalse(canClaim);
    }

    @Test
    public void CanClaimDrawByRepetitionWith_MoveMakingThirdOccurrence_ReturnsTrue() {
        Game game = gameBeforeLastMoveOfRoundTrip(ROUND_TRIPS_FOR_THIRD_OCCURRENCE);

        boolean canClaim = game.canClaimDrawByRepetitionWith(F6, G8);

        assertTrue(canClaim);
    }

    @Test
    public void CanClaimDrawByRepetitionWith_MoveMakingSecondOccurrence_ReturnsFalse() {
        Game game = gameBeforeLastMoveOfRoundTrip(ROUND_TRIPS_FOR_SECOND_OCCURRENCE);

        boolean canClaim = game.canClaimDrawByRepetitionWith(F6, G8);

        assertFalse(canClaim);
    }

    @Test
    public void CanClaimDrawByRepetitionWith_MoveToNewPosition_ReturnsFalse() {
        Game game = gameBeforeLastMoveOfRoundTrip(ROUND_TRIPS_FOR_THIRD_OCCURRENCE);

        boolean canClaim = game.canClaimDrawByRepetitionWith(B8, C6);

        assertFalse(canClaim);
    }

    @Test
    public void IsFivefoldRepetition_PositionSeenFourTimes_ReturnsFalse() {
        Game game = gameAfterKnightRoundTrips(ROUND_TRIPS_FOR_FOURTH_OCCURRENCE);

        boolean fivefold = game.isFivefoldRepetition();

        assertFalse(fivefold);
    }

    @Test
    public void IsFivefoldRepetition_PositionSeenFiveTimes_ReturnsTrue() {
        Game game = gameAfterKnightRoundTrips(ROUND_TRIPS_FOR_FIFTH_OCCURRENCE);

        boolean fivefold = game.isFivefoldRepetition();

        assertTrue(fivefold);
    }

    @Test
    public void IsDrawnByRepetition_StartedGame_ReturnsFalse() {
        Game game = new Game();
        game.start("Alice", "Bob");

        boolean drawn = game.isDrawnByRepetition();

        assertFalse(drawn);
    }

    @Test
    public void EndByRepetition_StartedGame_DrawsTheGame() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.endByRepetition();

        assertTrue(game.isOver());
        assertTrue(game.isDrawnByRepetition());
    }

    @Test
    public void EndByRepetition_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::endByRepetition);
    }

    @Test
    public void IsDrawnByDeadPosition_StartedGame_ReturnsFalse() {
        Game game = new Game();
        game.start("Alice", "Bob");

        boolean drawn = game.isDrawnByDeadPosition();

        assertFalse(drawn);
    }

    @Test
    public void EndByDeadPosition_StartedGame_DrawsTheGame() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.endByDeadPosition();

        assertTrue(game.isOver());
        assertTrue(game.isDrawnByDeadPosition());
    }

    @Test
    public void EndByCheckmate_StartedGame_IsNotDeadPosition() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.endByCheckmate();

        assertTrue(game.isOver());
        assertFalse(game.isDrawnByDeadPosition());
    }

    @Test
    public void WinnerName_AfterDeadPosition_ThrowsIllegalStateException() {
        Game game = new Game();
        game.start("Alice", "Bob");
        game.endByDeadPosition();

        assertThrows(IllegalStateException.class, game::winnerName);
    }

    @Test
    public void WinnerColor_AfterDeadPosition_ThrowsIllegalStateException() {
        Game game = new Game();
        game.start("Alice", "Bob");
        game.endByDeadPosition();

        assertThrows(IllegalStateException.class, game::winnerColor);
    }

    @Test
    public void EndByDeadPosition_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::endByDeadPosition);
    }

    @Test
    public void Resign_WhiteResigns_MakesPlayerTwoTheWinner() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.resign(PieceColor.WHITE);

        assertEquals("Bob", game.winnerName());
        assertEquals(PieceColor.BLACK, game.winnerColor());
    }

    @Test
    public void Resign_BlackResignsOnWhitesTurn_MakesPlayerOneTheWinner() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.resign(PieceColor.BLACK);

        assertEquals("Alice", game.winnerName());
        assertEquals(PieceColor.WHITE, game.winnerColor());
    }

    @Test
    public void Resign_StartedGame_EndsTheGameByResignation() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.resign(PieceColor.WHITE);

        assertTrue(game.isOver());
        assertTrue(game.isWonByResignation());
    }

    @Test
    public void Resign_AfterCheckmate_KeepsTheCheckmateWinner() {
        Game game = new Game();
        game.start("Alice", "Bob");
        game.endByCheckmate();

        game.resign(PieceColor.BLACK);

        assertEquals("Bob", game.winnerName());
        assertFalse(game.isWonByResignation());
    }

    @Test
    public void Resign_AfterDrawByRepetition_IsIgnored() {
        Game game = new Game();
        game.start("Alice", "Bob");
        game.endByRepetition();

        game.resign(PieceColor.WHITE);

        assertFalse(game.isWonByResignation());
    }

    @Test
    public void Resign_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, () -> game.resign(PieceColor.WHITE));
    }

    @Test
    public void IsWonByResignation_StartedGame_ReturnsFalse() {
        Game game = new Game();
        game.start("Alice", "Bob");

        boolean wonByResignation = game.isWonByResignation();

        assertFalse(wonByResignation);
    }

    private static Game gameAfterKnightRoundTrips(int roundTrips) {
        Game game = new Game();
        game.start("Alice", "Bob");
        for (int roundTrip = 0; roundTrip < roundTrips; roundTrip++) {
            playMove(game, G1, F3);
            playMove(game, G8, F6);
            playMove(game, F3, G1);
            playMove(game, F6, G8);
        }
        return game;
    }

    private static Game gameBeforeLastMoveOfRoundTrip(int roundTrips) {
        Game game = gameAfterKnightRoundTrips(roundTrips - 1);
        playMove(game, G1, F3);
        playMove(game, G8, F6);
        playMove(game, F3, G1);
        return game;
    }

    private static void playMove(Game game, Location from, Location to) {
        game.board().move(from, to);
        game.passTurn();
    }

    @Test
    public void IsPlayerToMoveInCheck_StartedGame_ReturnsFalse() {
        Game game = new Game();
        game.start("Alice", "Bob");

        boolean inCheck = game.isPlayerToMoveInCheck();

        assertFalse(inCheck);
    }

    @Test
    public void IsPlayerToMoveInCheck_WhiteToMoveInCheck_ReturnsTrue() {
        Game game = new Game();
        game.start("Alice", "Bob");
        playFoolsMate(game);

        boolean inCheck = game.isPlayerToMoveInCheck();

        assertTrue(inCheck);
    }

    @Test
    public void IsPlayerToMoveInCheck_BlackToMoveInCheck_ReturnsTrue() {
        Game game = new Game();
        game.start("Alice", "Bob");
        playQueenCheckOnBlack(game);

        boolean inCheck = game.isPlayerToMoveInCheck();

        assertTrue(inCheck);
    }

    @Test
    public void IsPlayerToMoveInCheck_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::isPlayerToMoveInCheck);
    }

    private void playFoolsMate(Game game) {
        playMove(game, F2, F3);
        playMove(game, E7, E5);
        playMove(game, G2, G4);
        playMove(game, D8, H4);
    }

    private void playQueenCheckOnBlack(Game game) {
        playMove(game, E2, E4);
        playMove(game, F7, F6);
        playMove(game, D1, H5);
    }

    @Test
    public void CanOfferDraw_NewGame_ReturnsFalse() {
        Game game = new Game();
        game.start("Alice", "Bob");

        boolean canOffer = game.canOfferDraw();

        assertFalse(canOffer);
    }

    @Test
    public void CanOfferDraw_AfterOneMove_ReturnsFalse() {
        Game game = gameAfterMoves(MOVES_BY_BOTH_PLAYERS - 1);

        boolean canOffer = game.canOfferDraw();

        assertFalse(canOffer);
    }

    @Test
    public void CanOfferDraw_AfterBothPlayersMoved_ReturnsTrue() {
        Game game = gameAfterMoves(MOVES_BY_BOTH_PLAYERS);

        boolean canOffer = game.canOfferDraw();

        assertTrue(canOffer);
    }

    @Test
    public void IsDrawOffered_StartedGame_ReturnsFalse() {
        Game game = new Game();
        game.start("Alice", "Bob");

        boolean offered = game.isDrawOffered();

        assertFalse(offered);
    }

    @Test
    public void OfferDraw_AfterBothPlayersMoved_MakesTheOfferPending() {
        Game game = gameAfterMoves(MOVES_BY_BOTH_PLAYERS);

        game.offerDraw(PieceColor.WHITE);

        assertTrue(game.isDrawOffered());
    }

    @Test
    public void OfferDraw_AfterOneMove_IsRefused() {
        Game game = gameAfterMoves(MOVES_BY_BOTH_PLAYERS - 1);

        game.offerDraw(PieceColor.BLACK);

        assertFalse(game.isDrawOffered());
    }

    @Test
    public void CanOfferDraw_OfferPending_ReturnsFalse() {
        Game game = gameAfterMoves(MOVES_BY_BOTH_PLAYERS);
        game.offerDraw(PieceColor.WHITE);

        boolean canOffer = game.canOfferDraw();

        assertFalse(canOffer);
    }

    @Test
    public void OfferDraw_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, () -> game.offerDraw(PieceColor.WHITE));
    }

    @Test
    public void IsDrawnByAgreement_StartedGame_ReturnsFalse() {
        Game game = new Game();
        game.start("Alice", "Bob");

        boolean drawn = game.isDrawnByAgreement();

        assertFalse(drawn);
    }

    @Test
    public void AcceptDrawOffer_OfferPending_DrawsTheGame() {
        Game game = gameAfterMoves(MOVES_BY_BOTH_PLAYERS);
        game.offerDraw(PieceColor.WHITE);

        game.acceptDrawOffer();

        assertTrue(game.isOver());
        assertTrue(game.isDrawnByAgreement());
    }

    @Test
    public void AcceptDrawOffer_NoOffer_DoesNothing() {
        Game game = gameAfterMoves(MOVES_BY_BOTH_PLAYERS);

        game.acceptDrawOffer();

        assertFalse(game.isOver());
    }

    @Test
    public void DeclineDrawOffer_OfferPending_EndsTheOffer() {
        Game game = gameAfterMoves(MOVES_BY_BOTH_PLAYERS);
        game.offerDraw(PieceColor.WHITE);

        game.declineDrawOffer();

        assertFalse(game.isDrawOffered());
        assertFalse(game.isOver());
    }

    @Test
    public void PassTurn_OpponentOfTheOfferingPlayerMoves_EndsTheOffer() {
        Game game = gameAfterMoves(MOVES_BY_BOTH_PLAYERS);
        game.offerDraw(PieceColor.BLACK);

        game.passTurn();

        assertFalse(game.isDrawOffered());
    }

    @Test
    public void PassTurn_OfferingPlayerMoves_KeepsTheOffer() {
        Game game = gameAfterMoves(MOVES_BY_BOTH_PLAYERS);
        game.offerDraw(PieceColor.WHITE);

        game.passTurn();

        assertTrue(game.isDrawOffered());
    }

    @Test
    public void IsDrawOffered_GameEndedByCheckmate_ReturnsFalse() {
        Game game = gameAfterMoves(MOVES_BY_BOTH_PLAYERS);
        game.offerDraw(PieceColor.WHITE);
        game.endByCheckmate();

        boolean offered = game.isDrawOffered();

        assertFalse(offered);
    }

    @Test
    public void CanOfferDraw_GameOver_ReturnsFalse() {
        Game game = gameAfterMoves(MOVES_BY_BOTH_PLAYERS);
        game.endByRepetition();

        boolean canOffer = game.canOfferDraw();

        assertFalse(canOffer);
    }

    @Test
    public void CanClaimDrawByFiftyMove_NewGame_ReturnsFalse() {
        Game game = new Game();
        game.start("Alice", "Bob");

        boolean canClaim = game.canClaimDrawByFiftyMove();

        assertFalse(canClaim);
    }

    @Test
    public void CanClaimDrawByFiftyMove_NinetyNineQuietHalfMoves_ReturnsFalse() {
        Game game = gameAfterKnightRoundTrips(ROUND_TRIPS_FOR_NINETY_NINE_HALF_MOVES);
        playMove(game, G1, F3);
        playMove(game, G8, F6);
        playMove(game, F3, G1);

        boolean canClaim = game.canClaimDrawByFiftyMove();

        assertFalse(canClaim);
    }

    @Test
    public void CanClaimDrawByFiftyMove_OneHundredQuietHalfMoves_ReturnsTrue() {
        Game game = gameAfterKnightRoundTrips(ROUND_TRIPS_FOR_ONE_HUNDRED_HALF_MOVES);

        boolean canClaim = game.canClaimDrawByFiftyMove();

        assertTrue(canClaim);
    }

    @Test
    public void CanClaimDrawByFiftyMove_OneHundredFortyNineQuietHalfMoves_ReturnsTrue() {
        Game game = gameAfterKnightRoundTrips(
                ROUND_TRIPS_FOR_ONE_HUNDRED_FORTY_NINE_HALF_MOVES);
        playMove(game, G1, F3);

        boolean canClaim = game.canClaimDrawByFiftyMove();

        assertTrue(canClaim);
    }

    @Test
    public void CanClaimDrawByFiftyMove_PawnMoveAfterOneHundred_ReturnsFalse() {
        Game game = gameAfterKnightRoundTrips(ROUND_TRIPS_FOR_ONE_HUNDRED_HALF_MOVES);
        playMove(game, E2, E4);

        boolean canClaim = game.canClaimDrawByFiftyMove();

        assertFalse(canClaim);
    }

    @Test
    public void CanClaimDrawByFiftyMove_CaptureAfterOneHundred_ReturnsFalse() {
        Game game = gameAfterKnightRoundTrips(ROUND_TRIPS_FOR_ONE_HUNDRED_HALF_MOVES);
        playMove(game, A1, A8);

        boolean canClaim = game.canClaimDrawByFiftyMove();

        assertFalse(canClaim);
    }

    @Test
    public void CanClaimDrawByFiftyMove_PawnPromotesAfterNinetyNine_ReturnsFalse() {
        Game game = gameAfterKnightRoundTrips(ROUND_TRIPS_FOR_NINETY_NINE_HALF_MOVES);
        playMove(game, G1, F3);
        playMove(game, G8, F6);
        playMove(game, F3, G1);
        game.board().move(A2, A8);
        game.board().promote(A8, PieceType.QUEEN);
        game.passTurn();

        boolean canClaim = game.canClaimDrawByFiftyMove();

        assertFalse(canClaim);
    }

    @Test
    public void CanClaimDrawByFiftyMoveWith_MoveMakingOneHundred_ReturnsTrue() {
        Game game = gameBeforeLastMoveOfRoundTrip(ROUND_TRIPS_FOR_ONE_HUNDRED_HALF_MOVES);

        boolean canClaim = game.canClaimDrawByFiftyMoveWith(F6, G8);

        assertTrue(canClaim);
    }

    @Test
    public void CanClaimDrawByFiftyMoveWith_MoveMakingNinetyNine_ReturnsFalse() {
        Game game = gameAfterKnightRoundTrips(ROUND_TRIPS_FOR_NINETY_NINE_HALF_MOVES);
        playMove(game, G1, F3);
        playMove(game, G8, F6);

        boolean canClaim = game.canClaimDrawByFiftyMoveWith(F6, G8);

        assertFalse(canClaim);
    }

    @Test
    public void CanClaimDrawByFiftyMoveWith_PawnMoveWouldReset_ReturnsFalse() {
        Game game = gameBeforeLastMoveOfRoundTrip(ROUND_TRIPS_FOR_ONE_HUNDRED_HALF_MOVES);

        boolean canClaim = game.canClaimDrawByFiftyMoveWith(E2, E4);

        assertFalse(canClaim);
    }

    @Test
    public void IsSeventyFiveMoveRule_OneHundredFortyNineQuietHalfMoves_ReturnsFalse() {
        Game game = gameAfterKnightRoundTrips(
                ROUND_TRIPS_FOR_ONE_HUNDRED_FORTY_NINE_HALF_MOVES);
        playMove(game, G1, F3);

        boolean seventyFive = game.isSeventyFiveMoveRule();

        assertFalse(seventyFive);
    }

    @Test
    public void IsSeventyFiveMoveRule_OneHundredFiftyQuietHalfMoves_ReturnsTrue() {
        Game game = gameAfterKnightRoundTrips(
                ROUND_TRIPS_FOR_ONE_HUNDRED_FORTY_NINE_HALF_MOVES);
        playMove(game, G1, F3);
        playMove(game, G8, F6);

        boolean seventyFive = game.isSeventyFiveMoveRule();

        assertTrue(seventyFive);
    }

    @Test
    public void IsDrawnByFiftyMove_StartedGame_ReturnsFalse() {
        Game game = new Game();
        game.start("Alice", "Bob");

        boolean drawn = game.isDrawnByFiftyMove();

        assertFalse(drawn);
    }

    @Test
    public void EndByFiftyMove_StartedGame_DrawsTheGame() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.endByFiftyMove();

        assertTrue(game.isOver());
        assertTrue(game.isDrawnByFiftyMove());
    }

    @Test
    public void EndBySeventyFiveMove_StartedGame_DrawsTheGame() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.endBySeventyFiveMove();

        assertTrue(game.isOver());
        assertTrue(game.isDrawnBySeventyFiveMove());
    }

    @Test
    public void EndByCheckmate_StartedGame_IsNotDrawnByFiftyMove() {
        Game game = new Game();
        game.start("Alice", "Bob");

        game.endByCheckmate();

        assertFalse(game.isDrawnByFiftyMove());
        assertFalse(game.isDrawnBySeventyFiveMove());
    }

    @Test
    public void EndByFiftyMove_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::endByFiftyMove);
    }

    @Test
    public void EndBySeventyFiveMove_UnstartedGame_ThrowsIllegalStateException() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, game::endBySeventyFiveMove);
    }

    @Test
    public void WinnerName_DrawnByFiftyMove_ThrowsIllegalStateException() {
        Game game = new Game();
        game.start("Alice", "Bob");
        game.endByFiftyMove();

        assertThrows(IllegalStateException.class, game::winnerName);
    }

    @Test
    public void RemainingTime_AtStart_IsStartingTimeForBothPlayers() {
        ControllableClock clock = new ControllableClock(CLOCK_START);
        Game game = new Game(clock);
        game.start("Alice", "Bob");

        assertEquals(ChessClock.STARTING_TIME, game.remainingTime(PieceColor.WHITE));
        assertEquals(ChessClock.STARTING_TIME, game.remainingTime(PieceColor.BLACK));
    }

    @Test
    public void RemainingTime_AfterOneMillisecond_WhiteHasUsedOneMillisecond() {
        ControllableClock clock = new ControllableClock(CLOCK_START);
        Game game = new Game(clock);
        game.start("Alice", "Bob");

        clock.advance(ONE_MILLISECOND);

        assertEquals(ChessClock.STARTING_TIME.minus(ONE_MILLISECOND),
                game.remainingTime(PieceColor.WHITE));
        assertEquals(ChessClock.STARTING_TIME, game.remainingTime(PieceColor.BLACK));
    }

    @Test
    public void PassTurn_AfterWhiteUsedOneMillisecond_SwitchesTheClocks() {
        ControllableClock clock = new ControllableClock(CLOCK_START);
        Game game = new Game(clock);
        game.start("Alice", "Bob");
        clock.advance(ONE_MILLISECOND);

        game.passTurn();
        clock.advance(ONE_MILLISECOND);

        assertEquals(ChessClock.STARTING_TIME.minus(ONE_MILLISECOND),
                game.remainingTime(PieceColor.WHITE));
        assertEquals(ChessClock.STARTING_TIME.minus(ONE_MILLISECOND),
                game.remainingTime(PieceColor.BLACK));
    }

    @Test
    public void IsWonOnTime_StartedGame_ReturnsFalse() {
        ControllableClock clock = new ControllableClock(CLOCK_START);
        Game game = new Game(clock);
        game.start("Alice", "Bob");

        assertFalse(game.isWonOnTime());
        assertFalse(game.isDrawnOnTime());
    }

    @Test
    public void CheckTime_WhiteOutOfTimeWithBlackMatingMaterial_BobWinsOnTime() {
        ControllableClock clock = new ControllableClock(CLOCK_START);
        Game game = new Game(clock);
        game.start("Alice", "Bob");
        clock.advance(ChessClock.STARTING_TIME);

        game.checkTime();

        assertTrue(game.isOver());
        assertTrue(game.isWonOnTime());
        assertEquals("Bob", game.winnerName());
        assertEquals(PieceColor.BLACK, game.winnerColor());
    }

    @Test
    public void CheckTime_BlackOutOfTimeWithWhiteMatingMaterial_AliceWinsOnTime() {
        ControllableClock clock = new ControllableClock(CLOCK_START);
        Game game = new Game(clock);
        game.start("Alice", "Bob");
        game.passTurn();
        clock.advance(ChessClock.STARTING_TIME);

        game.checkTime();

        assertTrue(game.isWonOnTime());
        assertEquals("Alice", game.winnerName());
        assertEquals(PieceColor.WHITE, game.winnerColor());
    }

    @Test
    public void CheckTime_OneMillisecondLeft_DoesNotEndTheGame() {
        ControllableClock clock = new ControllableClock(CLOCK_START);
        Game game = new Game(clock);
        game.start("Alice", "Bob");
        clock.advance(ChessClock.STARTING_TIME.minusMillis(1));

        game.checkTime();

        assertFalse(game.isOver());
    }

    @Test
    public void CheckTime_TimePastZero_StillEndsAsLossOnTime() {
        ControllableClock clock = new ControllableClock(CLOCK_START);
        Game game = new Game(clock);
        game.start("Alice", "Bob");
        clock.advance(ChessClock.STARTING_TIME.plusMillis(1));

        game.checkTime();

        assertTrue(game.isWonOnTime());
        assertEquals(Duration.ZERO, game.remainingTime(PieceColor.WHITE));
    }

    @Test
    public void CheckTime_OpponentHasOnlyBareKing_DrawsOnTime() {
        ControllableClock clock = new ControllableClock(CLOCK_START);
        Game game = new Game(clock);
        game.start("Alice", "Bob");
        leaveOnlyKings(game.board());
        clock.advance(ChessClock.STARTING_TIME);

        game.checkTime();

        assertTrue(game.isOver());
        assertTrue(game.isDrawnOnTime());
        assertFalse(game.isWonOnTime());
    }

    private static Game gameAfterMoves(int moves) {
        Game game = new Game();
        game.start("Alice", "Bob");
        for (int move = 0; move < moves; move++) {
            game.passTurn();
        }
        return game;
    }

    private static void leaveOnlyKings(Board board) {
        leaveOnly(board, E1, E8);
    }

    private static void leaveOnly(Board board, Location... kept) {
        List<Location> extras = extrasOn(board, kept);
        Location capturer = extras.get(0);
        for (int i = 1; i < extras.size(); i++) {
            Location target = extras.get(i);
            board.move(capturer, target);
            capturer = target;
        }
        board.move(E1, capturer);
        board.move(capturer, E1);
    }

    private static List<Location> extrasOn(Board board, Location[] kept) {
        List<Location> extras = new ArrayList<>();
        for (int file = Location.MIN_FILE; file <= Location.MAX_FILE; file++) {
            for (int rank = Location.MIN_RANK; rank <= Location.MAX_RANK; rank++) {
                Location square = new Location(file, rank);
                if (board.isOccupied(square) && !isKept(square, kept)) {
                    extras.add(square);
                }
            }
        }
        return extras;
    }

    private static boolean isKept(Location square, Location[] kept) {
        for (Location keep : kept) {
            if (square.isSameSquareAs(keep)) {
                return true;
            }
        }
        return false;
    }

    private static final class ControllableClock extends Clock {
        private Instant instant;

        private ControllableClock(Instant start) {
            instant = start;
        }

        private void advance(Duration amount) {
            instant = instant.plus(amount);
        }

        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return Clock.fixed(instant, zone);
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
