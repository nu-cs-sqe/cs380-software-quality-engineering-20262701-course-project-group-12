package ui;

import domain.piece.PieceColor;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JPanel;

public class OfferDrawView extends JPanel {
    private final BoardController boardController;

    public OfferDrawView(BoardController boardController, Runnable afterOffer) {
        this.boardController = boardController;
        addOfferButton("draw.offer.white", PieceColor.WHITE);
        addOfferButton("draw.offer.black", PieceColor.BLACK);
        refresh();
    }

    public void refresh() {
        boolean canOffer = boardController.canOfferDraw();
        for (Component button : getComponents()) {
            button.setEnabled(canOffer);
        }
    }

    private void addOfferButton(String textKey, PieceColor offeringColor) {
        JButton button = new JButton(Messages.text(textKey));
        button.addActionListener(event -> offer(offeringColor));
        add(button);
    }

    private void offer(PieceColor offeringColor) {
        boardController.offerDraw(offeringColor);
        if (boardController.isDrawOffered()) {
            if (opponentAccepts()) {
                boardController.acceptDrawOffer();
            } else {
                boardController.declineDrawOffer();
            }
        }
    }

    boolean opponentAccepts() {
        return false;
    }
}
