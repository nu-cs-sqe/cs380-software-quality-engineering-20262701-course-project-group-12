package ui;

import domain.piece.PieceColor;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ResignView extends JPanel {
    private final BoardController boardController;
    private final Runnable afterResign;

    public ResignView(BoardController boardController, Runnable afterResign) {
        this.boardController = boardController;
        this.afterResign = afterResign;
        addResignButton("resign.white", PieceColor.WHITE);
        addResignButton("resign.black", PieceColor.BLACK);
    }

    private void addResignButton(String textKey, PieceColor resigningColor) {
        JButton button = new JButton(Messages.text(textKey));
        button.addActionListener(event -> {
            boardController.resign(resigningColor);
            afterResign.run();
            disableButtons();
        });
        add(button);
    }

    private void disableButtons() {
        for (Component button : getComponents()) {
            button.setEnabled(false);
        }
    }
}
