package ui;

import javax.swing.JButton;

public class DrawClaimView extends JButton {
    private final BoardController boardController;

    public DrawClaimView(BoardController boardController, Runnable afterClaim) {
        super(Messages.text("draw.claim"));
        this.boardController = boardController;
        addActionListener(event -> {
            boardController.claimDraw();
            afterClaim.run();
            setEnabled(false);
        });
        refresh();
    }

    public void refresh() {
        setEnabled(boardController.canClaimDraw());
    }
}
