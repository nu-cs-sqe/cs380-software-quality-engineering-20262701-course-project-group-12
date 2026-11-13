package domain;

import domain.piece.Bishop;
import domain.piece.King;
import domain.piece.Knight;
import domain.piece.Pawn;
import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;
import domain.piece.Queen;
import domain.piece.Rook;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTests {
    private static final Location A1 = new Location(0, 1);
    private static final Location A2 = new Location(0, 2);
    private static final Location A3 = new Location(0, 3);
    private static final Location A4 = new Location(0, 4);
    private static final Location A5 = new Location(0, 5);
    private static final Location A6 = new Location(0, 6);
    private static final Location A7 = new Location(0, 7);
    private static final Location A8 = new Location(0, 8);
    private static final Location B1 = new Location(1, 1);
    private static final Location B7 = new Location(1, 7);
    private static final Location B8 = new Location(1, 8);
    private static final Location C1 = new Location(2, 1);
    private static final Location C6 = new Location(2, 6);
    private static final Location C7 = new Location(2, 7);
    private static final Location C8 = new Location(2, 8);
    private static final Location D1 = new Location(3, 1);
    private static final Location D3 = new Location(3, 3);
    private static final Location D4 = new Location(3, 4);
    private static final Location D5 = new Location(3, 5);
    private static final Location D6 = new Location(3, 6);
    private static final Location D7 = new Location(3, 7);
    private static final Location D8 = new Location(3, 8);
    private static final Location E1 = new Location(4, 1);
    private static final Location E2 = new Location(4, 2);
    private static final Location E3 = new Location(4, 3);
    private static final Location E4 = new Location(4, 4);
    private static final Location E5 = new Location(4, 5);
    private static final Location E6 = new Location(4, 6);
    private static final Location E7 = new Location(4, 7);
    private static final Location E8 = new Location(4, 8);
    private static final Location F2 = new Location(5, 2);
    private static final Location F3 = new Location(5, 3);
    private static final Location F6 = new Location(5, 6);
    private static final Location F7 = new Location(5, 7);
    private static final Location F8 = new Location(5, 8);
    private static final Location G1 = new Location(6, 1);
    private static final Location G2 = new Location(6, 2);
    private static final Location G4 = new Location(6, 4);
    private static final Location G6 = new Location(6, 6);
    private static final Location G8 = new Location(6, 8);
    private static final Location H1 = new Location(7, 1);
    private static final Location H2 = new Location(7, 2);
    private static final Location H4 = new Location(7, 4);
    private static final Location H5 = new Location(7, 5);
    private static final Location H6 = new Location(7, 6);
    private static final Location H7 = new Location(7, 7);
    private static final Location H8 = new Location(7, 8);
    private static final int STARTING_PIECE_COUNT = 32;

    @Test
    public void PieceAt_A1_ReturnsWhiteRook() {
        assertOccupant(0, 1, PieceType.ROOK, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_H1_ReturnsWhiteRook() {
        assertOccupant(7, 1, PieceType.ROOK, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_B1_ReturnsWhiteKnight() {
        assertOccupant(1, 1, PieceType.KNIGHT, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_C1_ReturnsWhiteBishop() {
        assertOccupant(2, 1, PieceType.BISHOP, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_D1_ReturnsWhiteQueen() {
        assertOccupant(3, 1, PieceType.QUEEN, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_E1_ReturnsWhiteKing() {
        assertOccupant(4, 1, PieceType.KING, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_A2_ReturnsWhitePawn() {
        assertOccupant(0, 2, PieceType.PAWN, PieceColor.WHITE);
    }

    @Test
    public void PieceAt_H2_ReturnsWhitePawn() {
        assertOccupant(7, 2, PieceType.PAWN, PieceColor.WHITE);
    }

    @Test
    public void IsOccupied_A3_ReturnsFalse() {
        assertNotOccupied(0, 3);
    }

    @Test
    public void IsOccupied_H3_ReturnsFalse() {
        assertNotOccupied(7, 3);
    }

    @Test
    public void IsOccupied_A6_ReturnsFalse() {
        assertNotOccupied(0, 6);
    }

    @Test
    public void IsOccupied_H6_ReturnsFalse() {
        assertNotOccupied(7, 6);
    }

    @Test
    public void PieceAt_A7_ReturnsBlackPawn() {
        assertOccupant(0, 7, PieceType.PAWN, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_H7_ReturnsBlackPawn() {
        assertOccupant(7, 7, PieceType.PAWN, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_A8_ReturnsBlackRook() {
        assertOccupant(0, 8, PieceType.ROOK, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_B8_ReturnsBlackKnight() {
        assertOccupant(1, 8, PieceType.KNIGHT, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_C8_ReturnsBlackBishop() {
        assertOccupant(2, 8, PieceType.BISHOP, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_D8_ReturnsBlackQueen() {
        assertOccupant(3, 8, PieceType.QUEEN, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_E8_ReturnsBlackKing() {
        assertOccupant(4, 8, PieceType.KING, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_H8_ReturnsBlackRook() {
        assertOccupant(7, 8, PieceType.ROOK, PieceColor.BLACK);
    }

    @Test
    public void PieceAt_EmptyA3_ThrowsIllegalArgumentException() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> board.pieceAt(A3));
    }

    @Test
    public void PieceAt_CalledTwiceOnA1_StillReturnsWhiteRook() {
        Board board = new Board();

        Piece first = board.pieceAt(A1);
        Piece second = board.pieceAt(A1);

        assertEquals(PieceType.ROOK, first.getType());
        assertEquals(PieceColor.WHITE, first.getColor());
        assertEquals(PieceType.ROOK, second.getType());
        assertEquals(PieceColor.WHITE, second.getColor());
    }

    @Test
    public void IsOccupied_A1_ReturnsTrue() {
        assertIsOccupied(0, 1);
    }

    @Test
    public void IsOccupied_H1_ReturnsTrue() {
        assertIsOccupied(7, 1);
    }

    @Test
    public void IsOccupied_H2_ReturnsTrue() {
        assertIsOccupied(7, 2);
    }

    @Test
    public void IsOccupied_A7_ReturnsTrue() {
        assertIsOccupied(0, 7);
    }

    @Test
    public void IsOccupied_A8_ReturnsTrue() {
        assertIsOccupied(0, 8);
    }

    @Test
    public void IsOccupied_H8_ReturnsTrue() {
        assertIsOccupied(7, 8);
    }

    @Test
    public void Move_A1ToEmptyA4_PlacesTheRookOnA4() {
        Board board = new Board();
        Piece whiteRook = board.pieceAt(A1);

        board.move(A1, A4);

        assertSame(whiteRook, board.pieceAt(A4));
    }

    @Test
    public void Move_A1ToEmptyA4_LeavesA1Empty() {
        Board board = new Board();

        board.move(A1, A4);

        assertFalse(board.isOccupied(A1));
    }

    @Test
    public void Move_H8ToEmptyH5_PlacesTheRookOnH5() {
        Board board = new Board();
        Piece blackRook = board.pieceAt(H8);

        board.move(H8, H5);

        assertSame(blackRook, board.pieceAt(H5));
    }

    @Test
    public void Move_H8ToEmptyH5_LeavesH8Empty() {
        Board board = new Board();

        board.move(H8, H5);

        assertFalse(board.isOccupied(H8));
    }

    @Test
    public void Move_A1ToOccupiedA8_ReplacesTheBlackRookWithTheWhiteRook() {
        Board board = new Board();
        Piece whiteRook = board.pieceAt(A1);

        board.move(A1, A8);

        assertSame(whiteRook, board.pieceAt(A8));
    }

    @Test
    public void Move_H8ToOccupiedH1_ReplacesTheWhiteRookWithTheBlackRook() {
        Board board = new Board();
        Piece blackRook = board.pieceAt(H8);

        board.move(H8, H1);

        assertSame(blackRook, board.pieceAt(H1));
    }

    @Test
    public void Move_A8ToOccupiedA1_ReplacesTheWhiteRookWithTheBlackRook() {
        Board board = new Board();
        Piece blackRook = board.pieceAt(A8);

        board.move(A8, A1);

        assertSame(blackRook, board.pieceAt(A1));
    }

    @Test
    public void Move_H1ToOccupiedH8_ReplacesTheBlackRookWithTheWhiteRook() {
        Board board = new Board();
        Piece whiteRook = board.pieceAt(H1);

        board.move(H1, H8);

        assertSame(whiteRook, board.pieceAt(H8));
    }

    @Test
    public void Move_A1ToA1_LeavesTheRookOnA1() {
        Board board = new Board();
        Piece whiteRook = board.pieceAt(A1);

        board.move(A1, A1);

        assertSame(whiteRook, board.pieceAt(A1));
    }

    @Test
    public void Move_FromEmptyA4_ThrowsIllegalArgumentException() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> board.move(A4, A5));
    }

    @Test
    public void Move_EmptyA4ToA1_LeavesTheRookOnA1() {
        Board board = new Board();
        Piece whiteRook = board.pieceAt(A1);

        assertThrows(IllegalArgumentException.class, () -> board.move(A4, A1));

        assertSame(whiteRook, board.pieceAt(A1));
    }

    @Test
    public void Move_A1ToOccupiedA8_TakesTheCapturedRookOffTheBoard() {
        Board board = new Board();

        board.move(A1, A8);

        assertEquals(STARTING_PIECE_COUNT - 1, countPieces(board));
    }

    @Test
    public void IsCheckmated_NewBoard_ReturnsFalse() {
        Board board = new Board();

        boolean checkmated = board.isCheckmated(PieceColor.WHITE);

        assertFalse(checkmated);
    }

    @Test
    public void IsCheckmated_InCheckWithNoReply_ReturnsTrue() {
        Board board = foolsMate();

        boolean checkmated = board.isCheckmated(PieceColor.WHITE);

        assertTrue(checkmated);
    }

    @Test
    public void IsCheckmated_InCheckWithReplyThatBlocks_ReturnsFalse() {
        Board board = queenCheckOnBlack();

        boolean checkmated = board.isCheckmated(PieceColor.BLACK);

        assertFalse(checkmated);
    }

    @Test
    public void IsCheckmated_FoolsMate_LeavesTheBoardUnchanged() {
        Board board = foolsMate();
        Piece blackQueen = board.pieceAt(H4);
        Piece whiteKing = board.pieceAt(E1);

        board.isCheckmated(PieceColor.WHITE);

        assertSame(blackQueen, board.pieceAt(H4));
        assertSame(whiteKing, board.pieceAt(E1));
        assertEquals(STARTING_PIECE_COUNT, countPieces(board));
    }

    @Test
    public void IsCheckmated_NotInCheckWithNoLegalMove_ReturnsFalse() {
        Board board = samLoydsStalemate();

        boolean checkmated = board.isCheckmated(PieceColor.BLACK);

        assertFalse(checkmated);
    }

    @Test
    public void PositionAfterMove_KnightToF3_MatchesTheBoardAfterThatMove() {
        Board board = new Board();
        Board movedBoard = new Board();
        movedBoard.move(G1, F3);

        Position position = board.positionAfterMove(G1, F3, PieceColor.BLACK);

        assertTrue(position.isSameAs(movedBoard.position(PieceColor.BLACK)));
    }

    @Test
    public void PositionAfterMove_KnightToF3_LeavesTheBoardUnchanged() {
        Board board = new Board();
        Piece whiteKnight = board.pieceAt(G1);

        board.positionAfterMove(G1, F3, PieceColor.BLACK);

        assertSame(whiteKnight, board.pieceAt(G1));
        assertFalse(board.isOccupied(F3));
    }

    @Test
    public void IsDeadPosition_NewBoard_ReturnsFalse() {
        Board board = new Board();

        boolean dead = board.isDeadPosition();

        assertFalse(dead);
    }

    @Test
    public void IsDeadPosition_KingVsKing_ReturnsTrue() {
        Board board = boardHoldingOnly(E1, E8);

        boolean dead = board.isDeadPosition();

        assertTrue(dead);
    }

    @Test
    public void IsDeadPosition_KingAndBishopVsKing_ReturnsTrue() {
        Board board = boardHoldingOnly(E1, E8, C1);

        boolean dead = board.isDeadPosition();

        assertTrue(dead);
    }

    @Test
    public void IsDeadPosition_KingAndKnightVsKing_ReturnsTrue() {
        Board board = boardHoldingOnly(E1, E8, B1);

        boolean dead = board.isDeadPosition();

        assertTrue(dead);
    }

    @Test
    public void IsInCheck_NewBoard_ReturnsFalse() {
        Board board = new Board();

        boolean inCheck = board.isInCheck(PieceColor.WHITE);

        assertFalse(inCheck);
    }

    @Test
    public void IsInCheck_WhiteKingReachedByBlackQueen_ReturnsTrue() {
        Board board = foolsMate();

        boolean inCheck = board.isInCheck(PieceColor.WHITE);

        assertTrue(inCheck);
    }

    @Test
    public void IsInCheck_BlackKingWhileWhiteIsInCheck_ReturnsFalse() {
        Board board = foolsMate();

        boolean inCheck = board.isInCheck(PieceColor.BLACK);

        assertFalse(inCheck);
    }

    @Test
    public void IsInCheck_BlackKingReachedByWhiteQueen_ReturnsTrue() {
        Board board = queenCheckOnBlack();

        boolean inCheck = board.isInCheck(PieceColor.BLACK);

        assertTrue(inCheck);
    }

    @Test
    public void IsInCheck_NoKingOfThatColor_ThrowsIllegalStateException() {
        Board board = new Board();
        board.move(D8, E1);

        assertThrows(IllegalStateException.class, () -> board.isInCheck(PieceColor.WHITE));
    }

    @Test
    public void Promote_WhitePawnToQueen_PlacesWhiteQueen() {
        Piece promoted = promoteWhitePawnOnA8(PieceType.QUEEN);

        assertInstanceOf(Queen.class, promoted);
        assertEquals(PieceColor.WHITE, promoted.getColor());
    }

    @Test
    public void Promote_WhitePawnToRook_PlacesWhiteRook() {
        Piece promoted = promoteWhitePawnOnA8(PieceType.ROOK);

        assertInstanceOf(Rook.class, promoted);
        assertEquals(PieceColor.WHITE, promoted.getColor());
    }

    @Test
    public void Promote_WhitePawnToBishop_PlacesWhiteBishop() {
        Piece promoted = promoteWhitePawnOnA8(PieceType.BISHOP);

        assertInstanceOf(Bishop.class, promoted);
        assertEquals(PieceColor.WHITE, promoted.getColor());
    }

    @Test
    public void Promote_WhitePawnToKnight_PlacesWhiteKnight() {
        Piece promoted = promoteWhitePawnOnA8(PieceType.KNIGHT);

        assertInstanceOf(Knight.class, promoted);
        assertEquals(PieceColor.WHITE, promoted.getColor());
    }

    @Test
    public void Promote_BlackPawnToQueen_PlacesBlackQueen() {
        Board board = new Board();
        board.move(H7, H1);

        board.promote(H1, PieceType.QUEEN);

        Piece promoted = board.pieceAt(H1);
        assertInstanceOf(Queen.class, promoted);
        assertEquals(PieceColor.BLACK, promoted.getColor());
    }

    @Test
    public void Promote_ToKing_ThrowsIllegalArgumentException() {
        Board board = boardWithWhitePawnOnA8();

        assertThrows(IllegalArgumentException.class, () -> board.promote(A8, PieceType.KING));
    }

    @Test
    public void Promote_ToPawn_ThrowsIllegalArgumentException() {
        Board board = boardWithWhitePawnOnA8();

        assertThrows(IllegalArgumentException.class, () -> board.promote(A8, PieceType.PAWN));
    }

    @Test
    public void Promote_RejectedChoice_LeavesThePawn() {
        Board board = boardWithWhitePawnOnA8();
        Piece whitePawn = board.pieceAt(A8);

        assertThrows(IllegalArgumentException.class, () -> board.promote(A8, PieceType.KING));

        assertSame(whitePawn, board.pieceAt(A8));
    }

    private Piece promoteWhitePawnOnA8(PieceType type) {
        Board board = boardWithWhitePawnOnA8();
        board.promote(A8, type);
        return board.pieceAt(A8);
    }

    private Board boardWithWhitePawnOnA8() {
        Board board = new Board();
        board.move(A2, A8);
        return board;
    }

    @Test
    public void CanBeCapturedEnPassant_WhitePawnThatJustMovedTwoRanks_ReturnsTrue() {
        Board board = new Board();
        board.move(E2, E4);

        boolean capturable = board.canBeCapturedEnPassant(E4);

        assertTrue(capturable);
    }

    @Test
    public void CanBeCapturedEnPassant_PawnThatJustMovedOneRank_ReturnsFalse() {
        Board board = new Board();
        board.move(E2, E3);

        boolean capturable = board.canBeCapturedEnPassant(E3);

        assertFalse(capturable);
    }

    @Test
    public void CanBeCapturedEnPassant_RookThatJustMovedTwoRanks_ReturnsFalse() {
        Board board = new Board();
        board.move(A1, A3);

        boolean capturable = board.canBeCapturedEnPassant(A3);

        assertFalse(capturable);
    }

    @Test
    public void CanBeCapturedEnPassant_PawnThatMovedTwoRanksBeforeAnotherMove_ReturnsFalse() {
        Board board = new Board();
        board.move(E2, E4);
        board.move(B8, C6);

        boolean capturable = board.canBeCapturedEnPassant(E4);

        assertFalse(capturable);
    }

    @Test
    public void CanBeCapturedEnPassant_BlackPawnThatJustMovedTwoRanks_ReturnsTrue() {
        Board board = new Board();
        board.move(D7, D5);

        boolean capturable = board.canBeCapturedEnPassant(D5);

        assertTrue(capturable);
    }

    @Test
    public void CanBeCapturedEnPassant_SquareOtherThanThePawnThatMoved_ReturnsFalse() {
        Board board = new Board();
        board.move(E2, E4);

        boolean capturable = board.canBeCapturedEnPassant(D4);

        assertFalse(capturable);
    }

    @Test
    public void Move_PawnCapturingEnPassant_TakesTheCapturedPawnOffTheBoard() {
        Board board = new Board();
        board.move(E2, E4);
        board.move(E4, E5);
        board.move(D7, D5);

        board.move(E5, D6);

        assertFalse(board.isOccupied(D5));
    }

    @Test
    public void Move_PawnCapturingEnPassant_LandsOnTheSquarePassedOver() {
        Board board = new Board();
        board.move(E2, E4);
        board.move(E4, E5);
        board.move(D7, D5);
        Piece capturingPawn = board.pieceAt(E5);

        board.move(E5, D6);

        assertSame(capturingPawn, board.pieceAt(D6));
    }

    @Test
    public void Move_BesidePawnThatMovedOneRankTwice_LeavesThatPawnOnTheBoard() {
        Board board = new Board();
        board.move(E2, E4);
        board.move(E4, E5);
        board.move(D7, D6);
        board.move(D6, D5);

        board.move(E5, D6);

        assertTrue(board.isOccupied(D5));
    }

    @Test
    public void Move_PieceOtherThanPawnBesidePawnThatMovedTwoRanks_LeavesThatPawnOnTheBoard() {
        Board board = new Board();
        board.move(C1, E5);
        board.move(D7, D5);

        board.move(E5, D6);

        assertTrue(board.isOccupied(D5));
    }

    @Test
    public void Board_A1_PlacesRook() {
        assertPieceClass(A1, Rook.class);
    }

    @Test
    public void Board_H8_PlacesRook() {
        assertPieceClass(H8, Rook.class);
    }

    @Test
    public void Board_B1_PlacesKnight() {
        assertPieceClass(B1, Knight.class);
    }

    @Test
    public void Board_G8_PlacesKnight() {
        assertPieceClass(G8, Knight.class);
    }

    @Test
    public void Board_C1_PlacesBishop() {
        assertPieceClass(C1, Bishop.class);
    }

    @Test
    public void Board_F8_PlacesBishop() {
        assertPieceClass(F8, Bishop.class);
    }

    @Test
    public void Board_D1_PlacesQueen() {
        assertPieceClass(D1, Queen.class);
    }

    @Test
    public void Board_D8_PlacesQueen() {
        assertPieceClass(D8, Queen.class);
    }

    @Test
    public void Board_E1_PlacesKing() {
        assertPieceClass(E1, King.class);
    }

    @Test
    public void Board_E8_PlacesKing() {
        assertPieceClass(E8, King.class);
    }

    @Test
    public void Board_A2_PlacesPawn() {
        assertPieceClass(A2, Pawn.class);
    }

    @Test
    public void Board_H7_PlacesPawn() {
        assertPieceClass(H7, Pawn.class);
    }

    private Board foolsMate() {
        Board board = new Board();
        board.move(F2, F3);
        board.move(E7, E5);
        board.move(G2, G4);
        board.move(D8, H4);
        return board;
    }

    private Board queenCheckOnBlack() {
        Board board = new Board();
        board.move(E2, E4);
        board.move(F7, F6);
        board.move(D1, H5);
        return board;
    }

    private Board samLoydsStalemate() {
        Board board = new Board();
        board.move(E2, E3);
        board.move(A7, A5);
        board.move(D1, H5);
        board.move(A8, A6);
        board.move(H5, A5);
        board.move(H7, H5);
        board.move(H2, H4);
        board.move(A6, H6);
        board.move(A5, C7);
        board.move(F7, F6);
        board.move(C7, D7);
        board.move(E8, F7);
        board.move(D7, B7);
        board.move(D8, D3);
        board.move(B7, B8);
        board.move(D3, H7);
        board.move(B8, C8);
        board.move(F7, G6);
        board.move(C8, E6);
        return board;
    }

    private Board boardHoldingOnly(Location... kept) {
        Board board = new Board();
        List<Location> extras = extrasOn(board, kept);
        Location capturer = extras.get(0);
        for (int i = 1; i < extras.size(); i++) {
            Location target = extras.get(i);
            board.move(capturer, target);
            capturer = target;
        }
        board.move(E1, capturer);
        board.move(capturer, E1);
        return board;
    }

    private List<Location> extrasOn(Board board, Location[] kept) {
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

    private boolean isKept(Location square, Location[] kept) {
        for (Location keep : kept) {
            if (square.isSameSquareAs(keep)) {
                return true;
            }
        }
        return false;
    }

    private int countPieces(Board board) {
        int pieces = 0;
        for (int file = Location.MIN_FILE; file <= Location.MAX_FILE; file++) {
            for (int rank = Location.MIN_RANK; rank <= Location.MAX_RANK; rank++) {
                if (board.isOccupied(new Location(file, rank))) {
                    pieces++;
                }
            }
        }
        return pieces;
    }

    private void assertPieceClass(Location square, Class<? extends Piece> pieceClass) {
        Board board = new Board();

        assertInstanceOf(pieceClass, board.pieceAt(square));
    }

    private void assertOccupant(int file, int rank, PieceType type, PieceColor color) {
        Board board = new Board();
        Piece piece = board.pieceAt(new Location(file, rank));

        assertEquals(type, piece.getType());
        assertEquals(color, piece.getColor());
    }

    private void assertIsOccupied(int file, int rank) {
        Board board = new Board();

        assertTrue(board.isOccupied(new Location(file, rank)));
    }

    private void assertNotOccupied(int file, int rank) {
        Board board = new Board();

        assertFalse(board.isOccupied(new Location(file, rank)));
    }
}
