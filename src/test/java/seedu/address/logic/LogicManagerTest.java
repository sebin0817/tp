package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.BIRTHDATE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DRUG_ALLERGY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GENDER_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NRIC_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ArchiveCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.UndoableCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private JsonAddressBookStorage addressBookStorage;

    private JsonUserPrefsStorage userPrefsStorage;

    private StorageManager storage;
    private Model model = new ModelManager();
    private LogicManager logic;


    @BeforeEach
    public void setUp() {
        addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        userPrefsStorage = new JsonUserPrefsStorage(
                temporaryFolder.resolve("userPrefs.json"));
        storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void addCommand_notFullHistory_success() {
        Person validPerson = new PersonBuilder().build();
        UndoableCommand addCommand = new AddCommand(validPerson, null);

        logic = new LogicManager(model, storage, new ArrayDeque<>());
        int currCommandHistorySize = logic.getCommandHistorySize();
        logic.addCommand(addCommand);

        assertEquals(currCommandHistorySize + 1, logic.getCommandHistorySize());
    }

    @Test
    public void addCommand_fullHistory_success() {
        Person validPerson = new PersonBuilder().build();
        Deque<UndoableCommand> currFullHistory = new ArrayDeque<>();
        for (int i = 0; i < LogicManager.COMMAND_HISTORY_SIZE_LIMIT; ++i) {
            currFullHistory.addFirst(new AddCommand(validPerson, null));
        }

        logic = new LogicManager(model, storage, currFullHistory);
        logic.addCommand(new AddCommand(validPerson, null));

        assertEquals(LogicManager.COMMAND_HISTORY_SIZE_LIMIT, logic.getCommandHistorySize());
    }

    @Test
    public void undoLastCommand_emptyHistory_success() {
        CommandResult expectedResult = new CommandResult("No commands left to undo.");
        logic = new LogicManager(model, storage, new ArrayDeque<>());
        int currCommandHistorySize = logic.getCommandHistorySize();
        CommandResult result = logic.undoLastCommand();

        assertEquals(expectedResult, result);
        assertEquals(currCommandHistorySize, logic.getCommandHistorySize());
    }

    @Test
    public void undoLastCommand_nonEmptyHistory_success() {
        Person validPerson = new PersonBuilder().build();
        Deque<UndoableCommand> nonEmptyHistory = new ArrayDeque<>();
        nonEmptyHistory.addFirst(new AddCommand(validPerson, new AddressBook()));
        CommandResult expectedResult = new CommandResult(AddCommand.MESSAGE_UNDO_ADD_SUCCESS);
        logic = new LogicManager(model, storage, nonEmptyHistory);
        int currCommandHistorySize = logic.getCommandHistorySize();
        CommandResult result = logic.undoLastCommand();

        assertEquals(expectedResult, result);
        assertEquals(currCommandHistorySize - 1, logic.getCommandHistorySize());
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_archiveThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }
    @Test
    public void execute_undoableCommand_success() throws Exception {
        Person validPerson = new PersonBuilder().build();
        String addCommand = AddCommand.COMMAND_WORD
                + " " + "ic/S1234567D n/Amy Bee g/F b/07-10-1999 p/85355255 e/amy@gmail.com";
        assertCommandSuccess(addCommand,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)), model);
    }

    @Test
    public void execute_undoCommand_success() throws Exception {
        Person validPerson = new PersonBuilder().build();
        String addCommand = AddCommand.COMMAND_WORD
                + " " + "ic/S1234567D n/Amy Bee g/F b/07-10-1999 p/85355255 e/amy@gmail.com";
        assertCommandSuccess(addCommand,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)), model);
        String undoCommand = UndoCommand.COMMAND_WORD;
        assertCommandSuccess(undoCommand, UndoCommand.MESSAGE_SUCCESS_UNDO
                + " " + AddCommand.MESSAGE_UNDO_ADD_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    @Test
    public void getAddressBook_retrieveAddressBook_containsExpectedData() {
        ReadOnlyAddressBook readOnlyAddressBook = logic.getAddressBook();
        assertEquals(model.getAddressBook(), readOnlyAddressBook);
    }

    @Test
    public void getFilteredNoteList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredNoteList().remove(0));
    }

    @Test
    public void getAddressBookFilePath_checkPath_matchesModel() {
        Path expectedPath = Paths.get("dummy/path/addressBook.json");
        model.setAddressBookFilePath(expectedPath);
        Path actualPath = logic.getAddressBookFilePath();
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void getGuiSettings_checkSettings_matchesModel() {
        GuiSettings expectedSettings = new GuiSettings(1, 2, 1024, 768);
        model.setGuiSettings(expectedSettings);
        GuiSettings actualSettings = logic.getGuiSettings();
        assertEquals(expectedSettings, actualSettings);
    }

    @Test
    public void setGuiSettings_checkSettings_effectInModel() {
        GuiSettings newSettings = new GuiSettings(1.5, 3, 1280, 720);
        logic.setGuiSettings(newSettings);
        GuiSettings modelSettings = model.getGuiSettings();
        assertEquals(newSettings, modelSettings);
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException, IOException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw e;
            }

            @Override
            public void copyAddressBook(ReadOnlyAddressBook addressBook, Path sourcePath, Path targetPath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + NRIC_DESC_AMY + NAME_DESC_AMY + GENDER_DESC_AMY
                + BIRTHDATE_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + DRUG_ALLERGY_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withIllnesses().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);

        // Triggers the copyAddressBook method by executing an archive command
        String archiveCommand = ArchiveCommand.COMMAND_WORD;
        assertCommandFailure(archiveCommand, CommandException.class, expectedMessage, expectedModel);
    }
}
