package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LocationTests {

    @Test
    public void Location_File0Rank1_StoresBothLows() {
        Location location = new Location(0, 1);

        assertEquals(0, location.getFile());
        assertEquals(1, location.getRank());
    }
}
