package domain;

import domain.piece.Piece;
import domain.piece.PieceColor;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class Position {
    private final PieceColor colorToMove;
    private final List<Optional<Piece>> occupants;

    Position(PieceColor colorToMove, List<Optional<Piece>> occupants) {
        this.colorToMove = colorToMove;
        this.occupants = occupants;
    }

    public boolean isSameAs(Position other) {
        return colorToMove == other.colorToMove && hasSamePiecesAs(other);
    }

    private boolean hasSamePiecesAs(Position other) {
        return IntStream.range(0, occupants.size()).allMatch(square ->
                holdSamePiece(occupants.get(square), other.occupants.get(square)));
    }

    private static boolean holdSamePiece(Optional<Piece> mine, Optional<Piece> theirs) {
        return mine.map(Piece::getType).equals(theirs.map(Piece::getType))
                && mine.map(Piece::getColor).equals(theirs.map(Piece::getColor));
    }
}
