package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

public class BirthDateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BirthDate(null));
    }

    @Test
    public void constructor_invalidBirthDate_throwsIllegalArgumentException() {
        String invalidBirthDate = "2022-01-01";
        assertThrows(IllegalArgumentException.class, () -> new BirthDate(invalidBirthDate));
    }

    @Test
    public void isValidBirthDate() {
        // null birthdate
        assertThrows(NullPointerException.class, () -> BirthDate.isValidBirthDate(null));

        // blank birthdate
        assertFalse(BirthDate.isValidBirthDate("")); // empty string
        assertFalse(BirthDate.isValidBirthDate(" ")); // spaces only

        // missing parts
        assertFalse(BirthDate.isValidBirthDate("01-01"));
        assertFalse(BirthDate.isValidBirthDate("01-2000"));

        // invalid parts
        assertFalse(BirthDate.isValidBirthDate("05-20-2000")); // invalid month
        assertFalse(BirthDate.isValidBirthDate("40-11-2000")); // invalid day
        assertFalse(BirthDate.isValidBirthDate("05-11-20")); // invalid year
        assertFalse(BirthDate.isValidBirthDate("01-01-9999")); // in the future

        // valid
        assertTrue(BirthDate.isValidBirthDate("01-11-2000"));
        assertTrue(BirthDate.isValidBirthDate("31-01-2000"));
    }

    @Test
    public void getAge() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = today.format(dateTimeFormatter);

        assertEquals(0, new BirthDate(formattedDate).getAge());

        String pastDate = today.minusYears(10).format(dateTimeFormatter);
        String pastDate2 = today.minusYears(10).plusMonths(2).format(dateTimeFormatter);
        String pastDate3 = today.minusYears(10).plusDays(2).format(dateTimeFormatter);

        assertEquals(10, new BirthDate(pastDate).getAge());
        assertEquals(9, new BirthDate(pastDate2).getAge());
        assertEquals(9, new BirthDate(pastDate3).getAge());
    }
}
