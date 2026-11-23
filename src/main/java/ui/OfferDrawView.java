package ui;

import domain.piece.PieceColor;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JPanel;

public class OfferDrawView extends JPanel {
    private final BoardController boardController;

    public OfferDrawView(BoardController boardController, Runnable afterOffer) {
        this.boardController = boardController;
        JButton whiteButton = new JButton(Messages.text("draw.offer.white"));
        whiteButton.addActionListener(event -> {
            boardController.offerDraw(PieceColor.WHITE);
            if (boardController.isDrawOffered() && opponentAccepts()) {
                boardController.acceptDrawOffer();
            }
        });
        add(whiteButton);
        add(new JButton(Messages.text("draw.offer.black")));
        refresh();
    }

    public void refresh() {
        boolean canOffer = boardController.canOfferDraw();
        for (Component button : getComponents()) {
            button.setEnabled(canOffer);
        }
    }

    boolean opponentAccepts() {
        return false;
    }
}
