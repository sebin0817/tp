package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the patient book.
 */
public class ClearCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "clear";

    public static final String MESSAGE_SUCCESS = "Patient book has been cleared!";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Clears the patient book.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_UNDO_CLEAR_SUCCESS = "Clear patient medical records undone.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        savePrevState(model);

        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public CommandResult undo(Model model) {
        model.setAddressBook(prevAddressBookState);
        return new CommandResult(MESSAGE_UNDO_CLEAR_SUCCESS);
    }

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public String getMessageUsage() {
        return MESSAGE_USAGE;
    }
}
