package seedu.address.model.person.illness;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class IllnessTest {
    private static final String VALID_ILLNESS_1 = "Infectious Diseases";
    private static final String VALID_ILLNESS_2 = "Chronic Conditions";
    private static final String INVALID_ILLNESS = "Motion Sickness";

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Illness(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Illness(invalidTagName));
    }

    @Test
    public void isValidIllnessName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Illness.isValidIllnessName(null));
    }

    @Test
    public void isValidIllnessNameFind_invalidKeywords_returnsFalse() {
        String[] invalidKeyword = INVALID_ILLNESS.split(" ");
        assertFalse(Illness.isValidIllnessFind(invalidKeyword[0]));
        assertFalse(Illness.isValidIllnessFind(invalidKeyword[1]));

        assertFalse(Illness.isValidIllnessFind(INVALID_ILLNESS));
        assertFalse(Illness.isValidIllnessFind(VALID_ILLNESS_1 + " " + INVALID_ILLNESS));
    }

    @Test
    public void isValidIllnessNameFind_validKeywords_returnsTrue() {
        String[] validKeyword = VALID_ILLNESS_1.split(" ");
        assertTrue(Illness.isValidIllnessFind(validKeyword[0]));
        assertTrue(Illness.isValidIllnessFind(validKeyword[1]));

        assertTrue(Illness.isValidIllnessFind(VALID_ILLNESS_1));
        assertTrue(Illness.isValidIllnessFind(VALID_ILLNESS_1 + " " + VALID_ILLNESS_2));

        assertTrue(Illness.isValidIllnessFind(VALID_ILLNESS_1 + "    \n\t   " + VALID_ILLNESS_2));
    }
}
