package seedu.address.logic.commands;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * Represents commands that can be undone by the user
 */
public abstract class UndoableCommand extends Command {
    protected ReadOnlyAddressBook prevAddressBookState;

    public UndoableCommand(ReadOnlyAddressBook addressBook) {
        this.prevAddressBookState = addressBook;
    }

    public ReadOnlyAddressBook getPrevAddressBookState() {
        return this.prevAddressBookState;
    }

    /**
     * Saves the current address book state as prev state.
     *
     * @param model - the model of the application containing the state.
     */
    protected void savePrevState(Model model) {
        this.prevAddressBookState = new AddressBook(model.getAddressBook());
    }

    /**
     * Undo the current command instance.
     *
     * @param model - the model of the application containing the state.
     * @return feedback message of the undo operation result for display
     */
    public abstract CommandResult undo(Model model);

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UndoableCommand)) {
            return false;
        }

        UndoableCommand otherUndoableCommand = (UndoableCommand) other;

        if (prevAddressBookState == null) {
            return otherUndoableCommand.prevAddressBookState == null;
        }

        return prevAddressBookState.equals(otherUndoableCommand.prevAddressBookState);
    }
}
