package seedu.address.logic;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.UndoableCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.note.Note;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app. Also acts as invoker.
 */
public class LogicManager implements Logic, CommandHistory {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";
    public static final int COMMAND_HISTORY_SIZE_LIMIT = 10; // limit to 10 most recent operations only


    private final Logger logger = LogsCenter.getLogger(LogicManager.class);
    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;
    private final Deque<UndoableCommand> commandHistoryDeque;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and
     * {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
        this.commandHistoryDeque = new ArrayDeque<>();
    }

    /**
     * Constructs a {@code LogicManager} with the given {@code Model}, {@code Storage} {@code ReadOnlyAddressBook}.
     */
    public LogicManager(Model model, Storage storage, Deque<UndoableCommand> commandHistoryDeque) {
        requireNonNull(commandHistoryDeque);
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
        this.commandHistoryDeque = commandHistoryDeque;
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = addressBookParser.parseCommand(commandText);
        commandResult = command.execute(model);

        if (command instanceof UndoCommand) {
            commandResult = new CommandResult(commandResult.getFeedbackToUser()
                    + " " + undoLastCommand().getFeedbackToUser());
        }

        // add to the history deque
        if (command instanceof UndoableCommand) {
            addCommand((UndoableCommand) command);
        }

        try {
            storage.saveAddressBook(model.getAddressBook());
            if (commandResult.isSave()) {
                storage.copyAddressBook(model.getAddressBook(), model.getAddressBookFilePath(),
                        model.getAddressBookArchivePath());
            }
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<Note> getFilteredNoteList() {
        return model.getFilteredNoteList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public void addCommand(UndoableCommand command) {
        if (getCommandHistorySize() == COMMAND_HISTORY_SIZE_LIMIT) {
            commandHistoryDeque.removeLast();
        }

        commandHistoryDeque.addFirst(command);
    }

    @Override
    public CommandResult undoLastCommand() {
        if (getCommandHistorySize() == 0) {
            return new CommandResult("No commands left to undo.");
        }

        UndoableCommand command = commandHistoryDeque.removeFirst();
        return command.undo(model);
    }

    @Override
    public int getCommandHistorySize() {
        return this.commandHistoryDeque.size();
    }
}
