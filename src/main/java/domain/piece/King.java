package domain.piece;

import domain.Board;
import domain.Location;

import java.util.stream.Stream;

public class King extends Piece {
    private static final int ONE_SQUARE = 1;
    private static final int CASTLING_DISTANCE = 2;

    public King(PieceColor color) {
        super(PieceType.KING, color);
    }

    @Override
    public boolean canMove(Location from, Location to, Board board) {
        if (holdsOwnPiece(to, board)) {
            return false;
        }
        if (isOneSquare(from, to)) {
            return true;
        }
        return isCastlingStep(from, to) && canCastle(from, to, board);
    }

    private boolean canCastle(Location from, Location to, Board board) {
        Location rookSquare = rookSquareFor(from, to);
        return board.holdsUnmovedPiece(from)
                && board.holdsUnmovedPiece(rookSquare)
                && isPathClear(from, rookSquare, board)
                && isKingPathSafe(from, to, board);
    }

    private Location rookSquareFor(Location from, Location to) {
        int rookFile = to.getFile() > from.getFile() ? Location.MAX_FILE : Location.MIN_FILE;
        return new Location(rookFile, from.getRank());
    }

    private boolean isKingPathSafe(Location from, Location to, Board board) {
        return Stream.of(from, crossedSquare(from, to), to)
                .noneMatch(square -> board.isAttackedByOpponentOf(getColor(), square));
    }

    private Location crossedSquare(Location from, Location to) {
        int towardRook = Integer.signum(to.getFile() - from.getFile());
        return new Location(from.getFile() + towardRook, from.getRank());
    }

    @Override
    public boolean attacks(Location from, Location target, Board board) {
        return isOneSquare(from, target);
    }

    @Override
    public void move(Location from, Location to, Board board) {
        super.move(from, to, board);
        if (isCastlingStep(from, to)) {
            board.move(rookSquareFor(from, to), crossedSquare(from, to));
        }
    }

    private boolean isOneSquare(Location from, Location to) {
        return Math.max(from.fileDistanceTo(to), from.rankDistanceTo(to)) == ONE_SQUARE;
    }

    private boolean isCastlingStep(Location from, Location to) {
        return from.getRank() == to.getRank() && from.fileDistanceTo(to) == CASTLING_DISTANCE;
    }
}
