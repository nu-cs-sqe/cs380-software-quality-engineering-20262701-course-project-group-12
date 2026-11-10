package ui;

import domain.Board;
import domain.Game;
import domain.Location;
import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardControllerTests {
    @Test
    public void IsSelected_NothingSelected_ReturnsFalse() {
        BoardController controller = replayedController();

        boolean isSelected = controller.isSelected(B1);

        assertFalse(isSelected);
        verify(board, piece);
    }

    @Test
    public void IsSelected_SelectedSquare_ReturnsTrue() {
        expectSelectableWhitePiece(B1);
        BoardController controller = replayedController();
        controller.clickSquare(B1);

        boolean isSelected = controller.isSelected(new Location(1, 1));

        assertTrue(isSelected);
        verify(board, piece);
    }

    @Test
    public void IsSelected_AnotherSquareWhileOneIsSelected_ReturnsFalse() {
        expectSelectableWhitePiece(B1);
        BoardController controller = replayedController();
        controller.clickSquare(B1);

        boolean isSelected = controller.isSelected(C3);

        assertFalse(isSelected);
        verify(board, piece);
    }

    @Test
    public void ClickSquare_SelectedSquareAgain_ClearsSelection() {
        expectSelectableWhitePiece(B1);
        BoardController controller = replayedController();
        controller.clickSquare(B1);

        controller.clickSquare(new Location(1, 1));

        assertFalse(controller.isSelected(B1));
        verify(board, piece);
    }

    private static final Location A1 = new Location(0, 1);
    private static final Location A3 = new Location(0, 3);
    private static final Location B1 = new Location(1, 1);
    private static final Location B3 = new Location(1, 3);
    private static final Location C3 = new Location(2, 3);
    private static final Location D2 = new Location(3, 2);
    private static final Location B8 = new Location(1, Location.MAX_RANK);
    private static final Location A2 = new Location(0, 2);
    private static final Location A7 = new Location(0, 7);
    private static final Location A8 = new Location(0, Location.MAX_RANK);
    private static final Piece BLACK_PAWN = new Piece(PieceType.PAWN, PieceColor.BLACK);

    private Board board;
    private Game game;
    private Piece piece;

    @BeforeEach
    public void createMocksForGameInProgress() {
        board = mock(Board.class);
        game = mock(Game.class);
        piece = mock(Piece.class);
        expect(game.isOver()).andStubReturn(false);
        expect(board.isCheckmated(anyObject(PieceColor.class))).andStubReturn(false);
    }

    @Test
    public void PieceAt_OccupiedSquare_ReturnsTheBoardsPiece() {
        Piece whiteRook = new Piece(PieceType.ROOK, PieceColor.WHITE);
        expect(board.pieceAt(A1)).andReturn(whiteRook);
        BoardController controller = replayedController();

        assertSame(whiteRook, controller.pieceAt(A1));
        verify(board);
    }

    @Test
    public void PieceAt_EmptySquare_ThrowsIllegalArgumentException() {
        expect(board.pieceAt(A3)).andThrow(new IllegalArgumentException());
        BoardController controller = replayedController();

        assertThrows(IllegalArgumentException.class, () -> controller.pieceAt(A3));
        verify(board);
    }

    @Test
    public void IsOccupied_BoardReportsPiece_ReturnsTrue() {
        expect(board.isOccupied(A1)).andReturn(true);
        BoardController controller = replayedController();

        assertTrue(controller.isOccupied(A1));
        verify(board);
    }

    @Test
    public void IsOccupied_BoardReportsAnEmptySquare_ReturnsFalse() {
        expect(board.isOccupied(A3)).andReturn(false);
        BoardController controller = replayedController();

        assertFalse(controller.isOccupied(A3));
        verify(board);
    }

    @Test
    public void ClickSquare_PieceThenSquareItCanReach_MovesPiece() {
        expectPieceOnB1();
        expect(board.isLegalMove(B1, C3)).andReturn(true);
        board.move(B1, C3);
        expect(piece.promotesOn(C3)).andReturn(false);
        game.passTurn();
        BoardController controller = replayedController();

        controller.clickSquare(B1);
        controller.clickSquare(C3);

        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_LegalMove_PassesTheTurn() {
        expectPieceOnB1();
        expect(board.isLegalMove(B1, C3)).andReturn(true);
        board.move(B1, C3);
        expect(piece.promotesOn(C3)).andReturn(false);
        game.passTurn();
        BoardController controller = replayedController();

        controller.clickSquare(B1);
        controller.clickSquare(C3);

        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_PieceThenSquareItCannotReach_LeavesBoardUnchanged() {
        expectSelectableWhitePiece(B1);
        expect(board.isLegalMove(B1, B3)).andReturn(false);
        expect(board.isOccupied(B3)).andReturn(false);
        BoardController controller = replayedController();

        controller.clickSquare(B1);
        controller.clickSquare(B3);

        verify(board, piece);
    }

    @Test
    public void ClickSquare_MoveTheBoardRefuses_LeavesBoardUnchanged() {
        expectSelectableWhitePiece(B1);
        expect(board.isLegalMove(B1, C3)).andReturn(false);
        expect(board.isOccupied(C3)).andReturn(false);
        BoardController controller = replayedController();

        controller.clickSquare(B1);
        controller.clickSquare(C3);

        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_EmptySquareFirst_SelectsNothing() {
        expect(board.isOccupied(A3)).andReturn(false);
        expect(board.isOccupied(C3)).andReturn(false);
        BoardController controller = replayedController();

        controller.clickSquare(A3);
        controller.clickSquare(C3);

        verify(board, piece);
    }

    @Test
    public void ClickSquare_AfterMove_SelectsNothing() {
        expectPieceOnB1();
        expect(board.isLegalMove(B1, C3)).andReturn(true);
        board.move(B1, C3);
        expect(piece.promotesOn(C3)).andReturn(false);
        game.passTurn();
        expect(board.isOccupied(A3)).andReturn(false);
        BoardController controller = replayedController();

        controller.clickSquare(B1);
        controller.clickSquare(C3);
        controller.clickSquare(A3);

        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_RefusedMoveOntoEmptySquare_KeepsSelection() {
        expectSelectableWhitePiece(B1);
        expect(board.isLegalMove(B1, B3)).andReturn(false);
        expect(board.isOccupied(B3)).andReturn(false);
        BoardController controller = replayedController();
        controller.clickSquare(B1);

        controller.clickSquare(B3);

        assertTrue(controller.isSelected(B1));
        verify(board, piece);
    }

    @Test
    public void ClickSquare_RefusedMoveOntoEmptySquare_DoesNotPassTheTurn() {
        expectSelectableWhitePiece(B1);
        expect(board.isLegalMove(B1, B3)).andReturn(false);
        expect(board.isOccupied(B3)).andReturn(false);
        BoardController controller = replayedController();

        controller.clickSquare(B1);
        controller.clickSquare(B3);

        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_PieceTheSelectedOneCannotReach_SelectsThatPiece() {
        expectSelectableWhitePiece(B1);
        expect(board.isLegalMove(B1, D2)).andReturn(false);
        expectSelectableWhitePiece(D2);
        BoardController controller = replayedController();
        controller.clickSquare(B1);

        controller.clickSquare(D2);

        assertTrue(controller.isSelected(D2));
        verify(board, piece);
    }

    @Test
    public void ClickSquare_OpponentPieceTheSelectedOneCanReach_CapturesIt() {
        expectPieceOnB1();
        expect(board.isLegalMove(B1, C3)).andReturn(true);
        givenOnC3(BLACK_PAWN);
        board.move(B1, C3);
        expect(piece.promotesOn(C3)).andReturn(false);
        game.passTurn();
        BoardController controller = replayedController();

        controller.clickSquare(B1);
        controller.clickSquare(C3);

        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_OpponentsPieceWithNothingSelected_SelectsNothing() {
        Piece opponent = mock(Piece.class);
        expect(board.isOccupied(B8)).andReturn(true);
        expect(board.pieceAt(B8)).andReturn(opponent);
        expect(opponent.getColor()).andReturn(PieceColor.BLACK);
        expect(game.colorToMove()).andReturn(PieceColor.WHITE);
        replay(board, piece, game, opponent);
        BoardController controller = new BoardController(board, game);

        controller.clickSquare(B8);

        assertFalse(controller.isSelected(B8));
        verify(board, piece, game, opponent);
    }

    @Test
    public void ClickSquare_AfterTurnPasses_DoesNotSelectPreviousPlayersPiece() {
        expectWhiteMoveFromB1ToC3ThenBlackToMove();
        Piece previousPlayerPiece = mock(Piece.class);
        expect(board.isOccupied(A2)).andReturn(true);
        expect(board.pieceAt(A2)).andReturn(previousPlayerPiece);
        expect(previousPlayerPiece.getColor()).andReturn(PieceColor.WHITE);
        expect(game.colorToMove()).andReturn(PieceColor.BLACK);
        replay(board, piece, game, previousPlayerPiece);
        BoardController controller = new BoardController(board, game);

        controller.clickSquare(B1);
        controller.clickSquare(C3);
        controller.clickSquare(A2);

        assertFalse(controller.isSelected(A2));
        verify(board, piece, game, previousPlayerPiece);
    }

    @Test
    public void ClickSquare_AfterTurnPasses_SelectsNewPlayersPiece() {
        expectWhiteMoveFromB1ToC3ThenBlackToMove();
        Piece newPlayerPiece = mock(Piece.class);
        expect(board.isOccupied(B8)).andReturn(true);
        expect(board.pieceAt(B8)).andReturn(newPlayerPiece);
        expect(newPlayerPiece.getColor()).andReturn(PieceColor.BLACK);
        expect(game.colorToMove()).andReturn(PieceColor.BLACK);
        replay(board, piece, game, newPlayerPiece);
        BoardController controller = new BoardController(board, game);

        controller.clickSquare(B1);
        controller.clickSquare(C3);
        controller.clickSquare(B8);

        assertTrue(controller.isSelected(B8));
        verify(board, piece, game, newPlayerPiece);
    }

    @Test
    public void ClickSquare_UnreachableOpponent_KeepsSelection() {
        expectSelectableWhitePiece(B1);
        expect(board.isLegalMove(B1, B8)).andReturn(false);
        Piece opponent = mock(Piece.class);
        expect(board.isOccupied(B8)).andReturn(true);
        expect(board.pieceAt(B8)).andReturn(opponent);
        expect(opponent.getColor()).andReturn(PieceColor.BLACK);
        replay(board, piece, game, opponent);
        BoardController controller = new BoardController(board, game);
        controller.clickSquare(B1);

        controller.clickSquare(B8);

        assertTrue(controller.isSelected(B1));
        assertFalse(controller.isSelected(B8));
        verify(board, piece, game, opponent);
    }

    @Test
    public void ClickSquare_MoveThatCheckmates_EndsTheGame() {
        expectWhiteMoveFromB1ToC3ThenBlackToMove();
        expect(board.isCheckmated(PieceColor.BLACK)).andReturn(true);
        game.endByCheckmate();
        BoardController controller = replayedController();

        controller.clickSquare(B1);
        controller.clickSquare(C3);

        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_MoveThatIsNotPromotion_LeavesNothingPending() {
        expectPieceOnB1();
        expect(board.isLegalMove(B1, C3)).andReturn(true);
        board.move(B1, C3);
        expect(piece.promotesOn(C3)).andReturn(false);
        game.passTurn();
        BoardController controller = replayedController();

        controller.clickSquare(B1);
        controller.clickSquare(C3);

        assertFalse(controller.isPromotionPending());
        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_MoveThatDoesNotCheckmate_DoesNotEndTheGame() {
        expectWhiteMoveFromB1ToC3ThenBlackToMove();
        expect(board.isCheckmated(PieceColor.BLACK)).andReturn(false);
        BoardController controller = replayedController();

        controller.clickSquare(B1);
        controller.clickSquare(C3);

        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_MoveOntoLastRank_LeavesPromotionPending() {
        expectPawnMoveFromA7ToA8();
        BoardController controller = replayedController();

        controller.clickSquare(A7);
        controller.clickSquare(A8);

        assertTrue(controller.isPromotionPending());
        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_MoveOntoLastRank_DoesNotPassTheTurn() {
        expectPawnMoveFromA7ToA8();
        BoardController controller = replayedController();

        controller.clickSquare(A7);
        controller.clickSquare(A8);

        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_RefusedMoveOntoLastRank_LeavesNothingPending() {
        expectSelectableWhitePiece(A7);
        expect(board.isLegalMove(A7, A8)).andReturn(false);
        expect(board.isOccupied(A8)).andReturn(false);
        BoardController controller = replayedController();

        controller.clickSquare(A7);
        controller.clickSquare(A8);

        assertFalse(controller.isPromotionPending());
        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_GameOver_SelectsNothing() {
        expect(game.isOver()).andReturn(true);
        BoardController controller = replayedController();

        controller.clickSquare(B1);

        assertFalse(controller.isSelected(B1));
        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_CaptureOntoLastRank_LeavesPromotionPending() {
        expectSelectableWhitePiece(A7);
        expect(board.pieceAt(A7)).andReturn(piece);
        expect(board.isLegalMove(A7, B8)).andReturn(true);
        board.move(A7, B8);
        expect(piece.promotesOn(B8)).andReturn(true);
        BoardController controller = replayedController();

        controller.clickSquare(A7);
        controller.clickSquare(B8);

        assertTrue(controller.isPromotionPending());
        verify(board, piece, game);
    }

    @Test
    public void ChoosePromotion_Queen_PromotesThePawnOnTheArrivalSquare() {
        expectPawnMoveFromA7ToA8();
        expectQueenPromotionOnA8();
        BoardController controller = controllerWithPromotionPendingOnA8();

        controller.choosePromotion(PieceType.QUEEN);

        verify(board, piece, game);
    }

    @Test
    public void ChoosePromotion_Queen_PassesTheTurn() {
        expectPawnMoveFromA7ToA8();
        expectQueenPromotionOnA8();
        BoardController controller = controllerWithPromotionPendingOnA8();

        controller.choosePromotion(PieceType.QUEEN);

        verify(board, piece, game);
    }

    @Test
    public void ChoosePromotion_Queen_LeavesNothingPending() {
        expectPawnMoveFromA7ToA8();
        expectQueenPromotionOnA8();
        BoardController controller = controllerWithPromotionPendingOnA8();

        controller.choosePromotion(PieceType.QUEEN);

        assertFalse(controller.isPromotionPending());
        verify(board, piece, game);
    }

    @Test
    public void ClickSquare_WhilePromotionIsPending_DoesNothing() {
        expectPawnMoveFromA7ToA8();
        BoardController controller = controllerWithPromotionPendingOnA8();

        controller.clickSquare(B1);

        assertFalse(controller.isSelected(B1));
        verify(board, piece, game);
    }

    private void expectWhiteMoveFromB1ToC3ThenBlackToMove() {
        expect(board.isOccupied(B1)).andReturn(true);
        expect(board.pieceAt(B1)).andReturn(piece);
        expect(piece.getColor()).andReturn(PieceColor.WHITE);
        expect(game.colorToMove()).andReturn(PieceColor.WHITE);
        expect(board.pieceAt(B1)).andReturn(piece);
        expect(board.isLegalMove(B1, C3)).andReturn(true);
        board.move(B1, C3);
        expect(piece.promotesOn(C3)).andReturn(false);
        game.passTurn();
        expect(game.colorToMove()).andReturn(PieceColor.BLACK);
    }

    @Test
    public void ChoosePromotion_King_ThrowsIllegalArgumentException() {
        expectPawnMoveFromA7ToA8();
        expectRejectedKingPromotionOnA8();
        BoardController controller = controllerWithPromotionPendingOnA8();

        assertThrows(IllegalArgumentException.class,
                () -> controller.choosePromotion(PieceType.KING));

        verify(board, piece, game);
    }

    @Test
    public void ChoosePromotion_RejectedChoice_KeepsPromotionPending() {
        expectPawnMoveFromA7ToA8();
        expectRejectedKingPromotionOnA8();
        BoardController controller = controllerWithPromotionPendingOnA8();
        assertThrows(IllegalArgumentException.class,
                () -> controller.choosePromotion(PieceType.KING));

        boolean pending = controller.isPromotionPending();

        assertTrue(pending);
        verify(board, piece, game);
    }

    @Test
    public void ChoosePromotion_PromotionThatCheckmates_EndsTheGame() {
        expectWhitePromotionToQueenOnA8ThenBlackToMove();
        expect(board.isCheckmated(PieceColor.BLACK)).andReturn(true);
        game.endByCheckmate();
        BoardController controller = controllerWithPromotionPendingOnA8();

        controller.choosePromotion(PieceType.QUEEN);

        verify(board, piece, game);
    }

    @Test
    public void ChoosePromotion_PromotionThatDoesNotCheckmate_DoesNotEndTheGame() {
        expectWhitePromotionToQueenOnA8ThenBlackToMove();
        expect(board.isCheckmated(PieceColor.BLACK)).andReturn(false);
        BoardController controller = controllerWithPromotionPendingOnA8();

        controller.choosePromotion(PieceType.QUEEN);

        verify(board, piece, game);
    }

    private BoardController controllerWithPromotionPendingOnA8() {
        BoardController controller = replayedController();
        controller.clickSquare(A7);
        controller.clickSquare(A8);
        return controller;
    }

    private void expectWhitePromotionToQueenOnA8ThenBlackToMove() {
        expect(board.isOccupied(A7)).andReturn(true);
        expect(board.pieceAt(A7)).andReturn(piece).times(2);
        expect(piece.getColor()).andReturn(PieceColor.WHITE);
        expect(game.colorToMove()).andReturn(PieceColor.WHITE);
        expect(board.isLegalMove(A7, A8)).andReturn(true);
        board.move(A7, A8);
        expect(piece.promotesOn(A8)).andReturn(true);
        board.promote(A8, PieceType.QUEEN);
        game.passTurn();
        expect(game.colorToMove()).andReturn(PieceColor.BLACK);
    }

    private void expectQueenPromotionOnA8() {
        board.promote(A8, PieceType.QUEEN);
        game.passTurn();
    }

    private void expectRejectedKingPromotionOnA8() {
        board.promote(A8, PieceType.KING);
        expectLastCall().andThrow(new IllegalArgumentException());
    }

    private void expectPawnMoveFromA7ToA8() {
        expectSelectableWhitePiece(A7);
        expect(board.pieceAt(A7)).andReturn(piece);
        expect(board.isLegalMove(A7, A8)).andReturn(true);
        board.move(A7, A8);
        expect(piece.promotesOn(A8)).andReturn(true);
    }

    private void expectPieceOnB1() {
        expectSelectableWhitePiece(B1);
        expect(board.pieceAt(B1)).andReturn(piece);
    }

    private void expectSelectableWhitePiece(Location square) {
        expect(board.isOccupied(square)).andReturn(true);
        expect(board.pieceAt(square)).andReturn(piece);
        expect(piece.getColor()).andStubReturn(PieceColor.WHITE);
        expect(game.colorToMove()).andStubReturn(PieceColor.WHITE);
    }

    private void givenOnC3(Piece occupant) {
        expect(board.isOccupied(C3)).andStubReturn(true);
        expect(board.pieceAt(C3)).andStubReturn(occupant);
    }

    private BoardController replayedController() {
        replay(board, piece, game);
        return new BoardController(board, game);
    }
}
