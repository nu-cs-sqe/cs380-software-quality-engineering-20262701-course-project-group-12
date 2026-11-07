package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationTests {
    @Test
    public void IsSameSquareAs_AnotherObjectForSameSquare_ReturnsTrue() {
        boolean isSameSquare = A1.isSameSquareAs(new Location(0, 1));

        assertTrue(isSameSquare);
    }

    @Test
    public void IsSameSquareAs_OneRankApart_ReturnsFalse() {
        boolean isSameSquare = A1.isSameSquareAs(new Location(0, 2));

        assertFalse(isSameSquare);
    }

    @Test
    public void IsSameSquareAs_OneFileApart_ReturnsFalse() {
        boolean isSameSquare = A1.isSameSquareAs(new Location(1, 1));

        assertFalse(isSameSquare);
    }

    private static final Location A1 = new Location(0, 1);

    @Test
    public void Location_File0Rank1_StoresBothLows() {
        Location location = new Location(0, 1);

        assertEquals(0, location.getFile());
        assertEquals(1, location.getRank());
    }

    @Test
    public void Location_File7Rank8_StoresBothHighs() {
        Location location = new Location(7, 8);

        assertEquals(7, location.getFile());
        assertEquals(8, location.getRank());
    }

    @Test
    public void Location_File0Rank8_StoresLowFileAndHighRank() {
        Location location = new Location(0, 8);

        assertEquals(0, location.getFile());
        assertEquals(8, location.getRank());
    }

    @Test
    public void Location_File7Rank1_StoresHighFileAndLowRank() {
        Location location = new Location(7, 1);

        assertEquals(7, location.getFile());
        assertEquals(1, location.getRank());
    }

    @Test
    public void Location_FileJustBelowInterval_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Location(-1, 1));
    }

    @Test
    public void Location_FileJustAboveInterval_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Location(8, 1));
    }

    @Test
    public void Location_RankJustBelowInterval_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Location(0, 0));
    }

    @Test
    public void Location_RankJustAboveInterval_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Location(0, 9));
    }
}
