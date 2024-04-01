package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteNoteCommand;

public class DeleteNoteCommandParserTest {
    private DeleteNoteCommandParser parser;

    @BeforeEach()
    public void setUp() {
        parser = new DeleteNoteCommandParser();
    }

    @Test
    public void parse_valid_success() {
        assertParseSuccess(parser, "1 1", new DeleteNoteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidChar_throwsParseException() {
        assertParseFailure(parser, "a b",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteNoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missing_throwsParseException() {
        assertParseFailure(parser, "1", String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteNoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_empty_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteNoteCommand.MESSAGE_USAGE));
    }
}
