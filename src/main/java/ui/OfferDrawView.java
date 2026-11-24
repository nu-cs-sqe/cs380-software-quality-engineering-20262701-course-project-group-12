package ui;

import domain.piece.PieceColor;

import java.util.EnumMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class OfferDrawView {
    private final Map<PieceColor, JButton> buttons = new EnumMap<>(PieceColor.class);
    private final BoardController boardController;
    private final Runnable afterOffer;

    public OfferDrawView(BoardController boardController, Runnable afterOffer) {
        this.boardController = boardController;
        this.afterOffer = afterOffer;
        for (PieceColor color : PieceColor.values()) {
            buttons.put(color, offerButton(color));
        }
        refresh();
    }

    public void refresh() {
        boolean canOffer = boardController.canOfferDraw();
        buttons.values().forEach(button -> button.setEnabled(canOffer));
    }

    public JButton buttonFor(PieceColor color) {
        return buttons.get(color);
    }

    private JButton offerButton(PieceColor offeringColor) {
        JButton button = new JButton(Messages.text("draw.offer"));
        button.addActionListener(event -> offer(offeringColor));
        return button;
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
                buttons.get(PieceColor.WHITE).getTopLevelAncestor(),
                Messages.text("draw.offer.question"),
                Messages.text("draw.offer.title"),
                JOptionPane.YES_NO_OPTION);
        return answer == JOptionPane.YES_OPTION;
    }
}
