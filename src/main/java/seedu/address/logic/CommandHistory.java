package seedu.address.logic;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.UndoableCommand;

/**
 * API of the CommandHistory component to handle recent command stack.
 */
public interface CommandHistory {

    /**
     * Adds undoable command into the history for undo feature.
     *
     * @param command - the undoable command entered by the user.
     */
    void addCommand(UndoableCommand command);

    /**
     * Returns command result after undoing the most recent
     * command from the history.
     *
     * @return CommandResult - the result output of the command undo.
     */
    CommandResult undoLastCommand();

    /**
     * @return int - the number of recent commands done by the user.
     */
    int getCommandHistorySize();
}
