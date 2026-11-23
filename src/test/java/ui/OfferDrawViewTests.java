package ui;

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
