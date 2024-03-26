package seedu.address.logic.parser;

import seedu.address.logic.commands.LoadCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new LoadCommand object
 */
public class LoadCommandParser implements Parser<LoadCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * HelpCommand and returns a HelpCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public LoadCommand parse(String args) throws ParseException {
        String trimmedFileId = args.trim();
        if (trimmedFileId.isEmpty()) {
            throw new ParseException(LoadCommand.MESSAGE_ID_NOT_PROVIDED);
        }
        int fileId = Integer.parseInt(trimmedFileId);
        if (fileId < 0) {
            throw new ParseException(LoadCommand.MESSAGE_ID_NOT_POSITIVE);
        }
        return new LoadCommand(fileId);
    }

}
