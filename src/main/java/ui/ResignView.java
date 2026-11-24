package ui;

import domain.piece.PieceColor;

import java.util.EnumMap;
import java.util.Map;
import javax.swing.JButton;

public class ResignView {
    private final Map<PieceColor, JButton> buttons = new EnumMap<>(PieceColor.class);
    private final BoardController boardController;
    private final Runnable afterResign;

    public ResignView(BoardController boardController, Runnable afterResign) {
        this.boardController = boardController;
        this.afterResign = afterResign;
        for (PieceColor color : PieceColor.values()) {
            buttons.put(color, resignButton(color));
        }
    }

    private JButton resignButton(PieceColor resigningColor) {
        JButton button = new JButton(Messages.text("resign"));
        button.addActionListener(event -> {
            boardController.resign(resigningColor);
            afterResign.run();
            disableButtons();
        });
        return button;
    }

    private void disableButtons() {
        buttons.values().forEach(button -> button.setEnabled(false));
    }

    public JButton buttonFor(PieceColor color) {
        return buttons.get(color);
    }
}
