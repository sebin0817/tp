package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonAddressBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonAddressBookStorageTest} class.
         */
        AddressBook original = getTypicalAddressBook();
        storageManager.saveAddressBook(original);
        ReadOnlyAddressBook retrieved = storageManager.readAddressBook().get();
        assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void copyAddressBook_success() throws Exception {
        // Setup: Create a StorageManager for the test
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(testFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(testFolder.resolve("userPrefs.json"));
        StorageManager storageManager = new StorageManager(addressBookStorage, userPrefsStorage);

        // Step 1: Prepare an original AddressBook
        AddressBook originalAddressBook = getTypicalAddressBook();

        // Step 2: Save this original AddressBook to a source path
        Path sourcePath = testFolder.resolve("sourceAddressBook.json");
        addressBookStorage.saveAddressBook(originalAddressBook, sourcePath);

        // Step 3: Use copyAddressBook to copy the AddressBook to a different target path
        Path targetPath = testFolder.resolve("targetAddressBook.json");
        storageManager.copyAddressBook(originalAddressBook, sourcePath, targetPath);

        // Verify that the copy exists
        assertTrue(Files.exists(targetPath));

        // Step 4: Read the AddressBook from the target path
        ReadOnlyAddressBook copiedAddressBook = addressBookStorage.readAddressBook(targetPath).get();

        // Verify that the original and the copied AddressBook are identical
        assertEquals(originalAddressBook, new AddressBook(copiedAddressBook));
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getAddressBookFilePath());
    }

}
