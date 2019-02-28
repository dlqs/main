package seedu.address.model.card;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class ScoreTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Score(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Score(invalidAddress));
    }

    @Test
    public void isValidScore() {
        // null address
        Assert.assertThrows(NullPointerException.class, () -> Score.isValidScore(null));

        // invalid addresses
        assertFalse(Score.isValidScore("")); // empty string
        assertFalse(Score.isValidScore(" ")); // spaces only
        assertFalse(Score.isValidScore("-10")); // one int
        assertFalse(Score.isValidScore("-10/25")); // negative int
        assertFalse(Score.isValidScore("10/-25")); // negative int
        assertFalse(Score.isValidScore("10.5/25")); // float

        // valid addresses
        assertTrue(Score.isValidScore("0/10"));
        assertTrue(Score.isValidScore("12/32")); // one character
        assertTrue(Score.isValidScore("71272/1823825")); // long address
    }
}
