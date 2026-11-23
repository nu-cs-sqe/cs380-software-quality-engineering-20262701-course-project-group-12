package ui;

import domain.Game;
import domain.piece.PieceColor;

import java.util.Map;

public class GameStatsController {
    private static final Map<PieceColor, String> CHECKMATE_KEYS = Map.of(
            PieceColor.WHITE, "stats.checkmate.white",
            PieceColor.BLACK, "stats.checkmate.black");
    private static final Map<PieceColor, String> RESIGNATION_KEYS = Map.of(
            PieceColor.WHITE, "stats.resignation.white",
            PieceColor.BLACK, "stats.resignation.black");

    private final Game game;
    private GameStatsView view;

    public GameStatsController(Game game) {
        if (!game.isStarted()) {
            throw new IllegalArgumentException(
                    "Cannot show game stats before the game has started");
        }
        this.game = game;
    }

    public GameStatsView createView() {
        view = new GameStatsView(game.playerOneName(), game.playerTwoName());
        refreshStatus();
        return view;
    }

    public void refreshStatus() {
        if (game.isOver()) {
            view.showResult(resultLine());
        } else if (game.isPlayerToMoveInCheck()) {
            view.showCheck(game.colorToMove(),
                    Messages.format("stats.check", game.playerToMoveName()));
        } else {
            view.showTurn(game.colorToMove(), currentPlayerLine());
        }
    }

    public String resultLine() {
        if (game.isDrawnByRepetition()) {
            return Messages.text("stats.drawByRepetition");
        }
        if (game.isDrawnByAgreement()) {
            return Messages.text("stats.drawByAgreement");
        }
        if (game.isStalemate()) {
            return Messages.text("stats.drawByStalemate");
        }
        if (game.isDrawnByDeadPosition()) {
            return Messages.text("stats.drawByDeadPosition");
        }
        if (game.isDrawnByFiftyMove()) {
            return Messages.text("stats.drawByFiftyMove");
        }
        if (game.isDrawnBySeventyFiveMove()) {
            return Messages.text("stats.drawBySeventyFiveMove");
        }
        if (game.isWonByResignation()) {
            return winnerLine(RESIGNATION_KEYS);
        }
        return winnerLine(CHECKMATE_KEYS);
    }

    private String winnerLine(Map<PieceColor, String> keysByWinnerColor) {
        return Messages.format(keysByWinnerColor.get(game.winnerColor()), game.winnerName());
    }

    private String currentPlayerLine() {
        return Messages.format("stats.currentPlayer", game.playerToMoveName());
    }
}
