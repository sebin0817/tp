package seedu.address.logic.commands;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * Represents commands that can be undone by the user
 */
public abstract class UndoableCommand extends Command {
    protected ReadOnlyAddressBook prevAddressBookState;

    /**
     * Saves the current address book state as prev state.
     *
     * @param model - the model of the application containing the state.
     */
    protected void savePrevState(Model model) {
        this.prevAddressBookState = new AddressBook(model.getAddressBook());
    }

    public abstract CommandResult undo(Model model);
}
