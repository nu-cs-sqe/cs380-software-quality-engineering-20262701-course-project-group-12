package ui;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JPanel;

public class OfferDrawView extends JPanel {
    private final BoardController boardController;

    public OfferDrawView(BoardController boardController, Runnable afterOffer) {
        this.boardController = boardController;
        add(new JButton(Messages.text("draw.offer.white")));
        add(new JButton(Messages.text("draw.offer.black")));
        refresh();
    }

    public void refresh() {
        boolean canOffer = boardController.canOfferDraw();
        for (Component button : getComponents()) {
            button.setEnabled(canOffer);
        }
    }
}
