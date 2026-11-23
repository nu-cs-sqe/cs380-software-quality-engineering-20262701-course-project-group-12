package ui;

import domain.piece.PieceColor;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class OfferDrawView extends JPanel {
    private final BoardController boardController;
    private final Runnable afterOffer;

    public OfferDrawView(BoardController boardController, Runnable afterOffer) {
        this.boardController = boardController;
        this.afterOffer = afterOffer;
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
        afterOffer.run();
        refresh();
    }

    boolean opponentAccepts() {
        int answer = JOptionPane.showConfirmDialog(
                this,
                Messages.text("draw.offer.question"),
                Messages.text("draw.offer.title"),
                JOptionPane.YES_NO_OPTION);
        return answer == JOptionPane.YES_OPTION;
    }
}
