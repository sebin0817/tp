package seedu.address.logic.commands;

import seedu.address.model.ReadOnlyAddressBook;

/**
 * Represents commands that can be undone by the user
 */
public abstract class UndoableCommand extends Command {
    protected ReadOnlyAddressBook prevState;

    /**
     * Returns command result after undoing the command.
     *
     * @return CommandResult.
     */
    public abstract CommandResult undo();
}
