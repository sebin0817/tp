package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Archives the current state of the patient book to the file.
 */
public class ArchiveCommand extends Command {

    public static final String COMMAND_WORD = "archive";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Archives the current state of the patient book to the file. "
        + "The current file accompanied with a date will be generated for the archived file. "
        + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "File archived.";

    /**
     * Executes the command and returns the result.
     *
     * @param model {@code Model} which the command should operate on.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        return new CommandResult(MESSAGE_SUCCESS, false, true, false);
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
