package domain.piece;

import domain.Board;
import domain.Location;

import java.util.stream.IntStream;

public class Queen extends Piece {
    private static final int FIRST_STEP_AFTER_START = 1;

    public Queen(PieceColor color) {
        super(PieceType.QUEEN, color);
    }

    @Override
    public boolean canMove(Location from, Location to, Board board) {
        return isAlongLine(from, to)
                && !isSameSquare(from, to)
                && !holdsOwnPiece(to, board)
                && isPathClear(from, to, board);
    }

    private boolean isAlongLine(Location from, Location to) {
        int fileDistance = fileDistance(from, to);
        int rankDistance = rankDistance(from, to);
        return fileDistance == 0 || rankDistance == 0 || fileDistance == rankDistance;
    }

    private boolean isSameSquare(Location from, Location to) {
        return from.getFile() == to.getFile() && from.getRank() == to.getRank();
    }

    private boolean holdsOwnPiece(Location square, Board board) {
        return board.isOccupied(square) && board.pieceAt(square).getColor() == getColor();
    }

    private boolean isPathClear(Location from, Location to, Board board) {
        int fileStep = Integer.signum(to.getFile() - from.getFile());
        int rankStep = Integer.signum(to.getRank() - from.getRank());
        int distance = Math.max(fileDistance(from, to), rankDistance(from, to));
        return IntStream.range(FIRST_STEP_AFTER_START, distance)
                .mapToObj(step -> new Location(
                        from.getFile() + step * fileStep, from.getRank() + step * rankStep))
                .noneMatch(board::isOccupied);
    }

    private int fileDistance(Location from, Location to) {
        return Math.abs(to.getFile() - from.getFile());
    }

    private int rankDistance(Location from, Location to) {
        return Math.abs(to.getRank() - from.getRank());
    }
}
