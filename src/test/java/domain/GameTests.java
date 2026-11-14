package domain;

import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location B8 = new Location(1, 8);
    private static final Location C6 = new Location(2, 6);
    private static final Location D1 = new Location(3, 1);
    private static final Location D8 = new Location(3, 8);
    private static final Location E2 = new Location(4, 2);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location E7 = new Location(4, 7);
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
    private static final int MOVES_BY_BOTH_PLAYERS = 2;

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

    private static Game gameAfterMoves(int moves) {
        Game game = new Game();
        game.start("Alice", "Bob");
        for (int move = 0; move < moves; move++) {
            game.passTurn();
        }
        return game;
    }
}
