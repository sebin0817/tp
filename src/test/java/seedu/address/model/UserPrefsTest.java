package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;


public class UserPrefsTest {

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setAddressBookFilePath(null));
    }

    @Test
    public void getAddressBookArchivePath_correctFormat() {
        UserPrefs userPrefs = new UserPrefs();
        Path archivePath = userPrefs.getAddressBookArchivePath();

        // Check if the path contains the expected directory
        assertTrue(archivePath.toString().contains(userPrefs.getAddressBookDirectory()));

        // Check if the path contains the expected base file name
        assertTrue(archivePath.toString().contains(userPrefs.getAddressBookFile()));

        // Check if the path ends with the expected file type
        assertTrue(archivePath.toString().endsWith(userPrefs.getAddressBookType()));

        // Check if the path contains a datetime string
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = dtf.format(LocalDateTime.now());
        assertTrue(archivePath.toString().contains(dateString));
    }

}
