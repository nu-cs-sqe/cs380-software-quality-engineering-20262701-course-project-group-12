package ui;

import domain.piece.PieceColor;
import org.junit.jupiter.api.Test;

import java.awt.Component;
import javax.swing.JButton;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OfferDrawViewTests {
    @Test
    public void Refresh_WhenCannotOffer_DisablesBothButtons() {
        BoardController boardController = createMock(BoardController.class);
        expect(boardController.canOfferDraw()).andStubReturn(false);
        replay(boardController);
        OfferDrawView view = new OfferDrawView(boardController, () -> { });

        view.refresh();

        assertFalse(whiteButton(view).isEnabled());
        assertFalse(blackButton(view).isEnabled());
        verify(boardController);
    }

    @Test
    public void Refresh_WhenCanOffer_EnablesBothButtons() {
        BoardController boardController = createMock(BoardController.class);
        expect(boardController.canOfferDraw()).andStubReturn(true);
        replay(boardController);
        OfferDrawView view = new OfferDrawView(boardController, () -> { });

        view.refresh();

        assertTrue(whiteButton(view).isEnabled());
        assertTrue(blackButton(view).isEnabled());
        verify(boardController);
    }

    @Test
    public void Buttons_AfterConstruction_ShowMessagesLabels() {
        BoardController boardController = createMock(BoardController.class);
        expect(boardController.canOfferDraw()).andStubReturn(false);
        replay(boardController);

        OfferDrawView view = new OfferDrawView(boardController, () -> { });

        assertEquals("White offers draw", whiteButton(view).getText());
        assertEquals("Black offers draw", blackButton(view).getText());
        verify(boardController);
    }

    @Test
    public void WhiteOffer_OpponentAccepts_AsksTheControllerToAccept() {
        BoardController boardController = createMock(BoardController.class);
        expect(boardController.canOfferDraw()).andStubReturn(true);
        boardController.offerDraw(PieceColor.WHITE);
        expect(boardController.isDrawOffered()).andReturn(true);
        boardController.acceptDrawOffer();
        replay(boardController);
        OfferDrawView view = acceptingView(boardController);

        whiteButton(view).doClick();

        verify(boardController);
    }

    @Test
    public void BlackOffer_OpponentDeclines_AsksTheControllerToDecline() {
        BoardController boardController = createMock(BoardController.class);
        expect(boardController.canOfferDraw()).andStubReturn(true);
        boardController.offerDraw(PieceColor.BLACK);
        expect(boardController.isDrawOffered()).andReturn(true);
        boardController.declineDrawOffer();
        replay(boardController);
        OfferDrawView view = decliningView(boardController);

        blackButton(view).doClick();

        verify(boardController);
    }

    @Test
    public void DialogTexts_FromMessages_MatchOfferDrawCopy() {
        assertEquals("Draw offer", Messages.text("draw.offer.title"));
        assertEquals("Accept the draw offer?", Messages.text("draw.offer.question"));
    }

    private static OfferDrawView acceptingView(BoardController boardController) {
        return new OfferDrawView(boardController, () -> { }) {
            @Override
            boolean opponentAccepts() {
                return true;
            }
        };
    }

    private static OfferDrawView decliningView(BoardController boardController) {
        return new OfferDrawView(boardController, () -> { }) {
            @Override
            boolean opponentAccepts() {
                return false;
            }
        };
    }

    private static JButton whiteButton(OfferDrawView view) {
        return buttonAt(view, 0);
    }

    private static JButton blackButton(OfferDrawView view) {
        return buttonAt(view, 1);
    }

    private static JButton buttonAt(OfferDrawView view, int index) {
        Component component = view.getComponent(index);
        return (JButton) component;
    }
}
