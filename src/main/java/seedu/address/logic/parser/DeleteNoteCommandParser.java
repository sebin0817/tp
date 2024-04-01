package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteNoteCommand object
 */
public class DeleteNoteCommandParser implements Parser<DeleteNoteCommand> {
    @Override
    public DeleteNoteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        Index patientIndex;
        Index noteIndex;

        try {
            Index[] indices = ParserUtil.parseIndices(argMultimap.getPreamble());
            patientIndex = indices[0];
            noteIndex = indices[1];
        } catch (ParseException | ArrayIndexOutOfBoundsException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteNoteCommand.MESSAGE_USAGE), e);
        }

        return new DeleteNoteCommand(patientIndex, noteIndex);
    }
}
