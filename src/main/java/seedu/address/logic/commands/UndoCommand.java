package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents the undo command itself of the application.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo the most recent command.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS_UNDO = "Undo command detected:";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return new CommandResult(MESSAGE_SUCCESS_UNDO);
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
