package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Loads the state of the patient book from the file with the specified id.
 */
public class LoadCommand extends Command {

    public static final String COMMAND_WORD = "load";

    public static final String MESSAGE_ID_NOT_PROVIDED = "Please provide the id of the file to load";

    public static final String MESSAGE_ID_NOT_POSITIVE = "The id must be a positive integer";

    public static final String MESSAGE_FILE_NOT_FOUND = "The file with the specified id does not exist";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Load the state of the patient book from the file with the specified id. "
        + "Example: " + COMMAND_WORD + " 1";

    public final Integer id;

    public LoadCommand(int id) {
        this.id = id;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        // TODO: Implement loading a file
        // 1. Copy the state of specified addressbook-<id>.json into current
        // addressbook.json
        return new CommandResult("File of id " + id + " loaded successfully");
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
