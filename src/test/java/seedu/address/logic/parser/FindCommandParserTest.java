package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.Person;
import seedu.address.model.person.predicates.BirthdateContainsKeywordsPredicate;
import seedu.address.model.person.predicates.DrugAllergyContainsKeywordsPredicate;
import seedu.address.model.person.predicates.EmailContainsKeywordsPredicate;
import seedu.address.model.person.predicates.GenderContainsKeywordsPredicate;
import seedu.address.model.person.predicates.IllnessContainsKeywordsPredicate;
import seedu.address.model.person.predicates.NameContainsKeywordsPredicate;
import seedu.address.model.person.predicates.NricContainsKeywordsPredicate;
import seedu.address.model.person.predicates.PhoneContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nameArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Predicate<Person> predicate = new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
        FindCommand expectedFindCommand = (FindCommand) new FindCommand(predicate);
        assertParseSuccess(parser, " n/Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " n/Alice   Bob    " + "\t" + "\n", expectedFindCommand);
    }

    @Test
    public void parse_nricArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Predicate<Person> predicate = new NricContainsKeywordsPredicate(Arrays.asList("S1234567D", "S3334567D"));
        FindCommand expectedFindCommand = (FindCommand) new FindCommand(predicate);
        assertParseSuccess(parser, " ic/S1234567D S3334567D", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " ic/S1234567D   S3334567D    " + "\t" + "\n", expectedFindCommand);
    }

    @Test
    public void parse_genderArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Predicate<Person> predicate = new GenderContainsKeywordsPredicate(Arrays.asList("M", "F"));
        FindCommand expectedFindCommand = (FindCommand) new FindCommand(predicate);
        assertParseSuccess(parser, " g/M F", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " n/M   F    " + "\t" + "\n", expectedFindCommand);
    }

    @Test
    public void parse_birthdateArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Predicate<Person> predicate = new BirthdateContainsKeywordsPredicate(Arrays.asList("11-11-2000", "12-12-2000"));
        FindCommand expectedFindCommand = (FindCommand) new FindCommand(predicate);
        assertParseSuccess(parser, " b/11-11-2000 12-12-2000", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " b/11-11-2000    12-12-2000    " + "\t" + "\n", expectedFindCommand);
    }

    @Test
    public void parse_phoneArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Predicate<Person> predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("81112222", "91112222"));
        FindCommand expectedFindCommand = (FindCommand) new FindCommand(predicate);
        assertParseSuccess(parser, " p/81112222 91112222", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " n/81112222    91112222    " + "\t" + "\n", expectedFindCommand);
    }

    @Test
    public void parse_emailArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Predicate<Person> predicate = new EmailContainsKeywordsPredicate(
                Arrays.asList("john@example.com", "bob@example.com"));
        FindCommand expectedFindCommand = (FindCommand) new FindCommand(predicate);
        assertParseSuccess(parser, " e/john@example.com bob@example.com", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " e/john@example.com  bob@example.com    " + "\t" + "\n", expectedFindCommand);
    }

    @Test
    public void parse_drugAllergyArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Predicate<Person> predicate = new DrugAllergyContainsKeywordsPredicate(
                Arrays.asList("Antibiotics", "Penicillin"));
        FindCommand expectedFindCommand = (FindCommand) new FindCommand(predicate);
        assertParseSuccess(parser, " d/Antibiotics Penicillin", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " d/Antibiotics    Penicillin    " + "\t" + "\n", expectedFindCommand);
    }

    @Test
    public void parse_illnessArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        Predicate<Person> predicate = new IllnessContainsKeywordsPredicate(
                Arrays.asList("Infectious", "Diseases"));
        FindCommand expectedFindCommand = (FindCommand) new FindCommand(predicate);
        assertParseSuccess(parser, " i/Infectious Diseases", expectedFindCommand);

        // multiple whitespaces between keywords
        // assertParseSuccess(parser, " i/Infectious    Diseases   " + "\t" + "\n", expectedFindCommand);
    }
}
