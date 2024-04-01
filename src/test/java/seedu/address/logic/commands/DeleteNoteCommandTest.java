package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_FLU;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;

@ExtendWith(MockitoExtension.class)
public class DeleteNoteCommandTest {
    @Mock
    private ModelManager model;

    @BeforeEach()
    public void setUp() {
        Mockito.reset(model);
    }

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeleteNoteCommand(null, null));
        assertThrows(NullPointerException.class, () -> new DeleteNoteCommand(Index.fromOneBased(1), null));
        assertThrows(NullPointerException.class, () -> new DeleteNoteCommand(null, Index.fromOneBased(1)));
    }

    @Nested
    public class ExecuteTests {
        @Test
        public void execute_personIndexOutOfBounds_throwsCommandException() {
            ObservableList<Person> persons = TypicalPersons.getTypicalAddressBook().getPersonList();

            Mockito.when(model.getFilteredPersonList()).thenReturn(new FilteredList<>(persons));
            DeleteNoteCommand command =
                new DeleteNoteCommand(Index.fromOneBased(persons.size() + 1), Index.fromOneBased(1));

            assertThrows(CommandException.class, () -> command.execute(model));
        }

        @Test
        public void execute_noteIndexOutOfBounds_throwsCommandException() {
            ObservableList<Person> persons = TypicalPersons.getTypicalAddressBook().getPersonList();

            Mockito.when(model.getFilteredPersonList()).thenReturn(new FilteredList<>(persons));
            DeleteNoteCommand command =
                new DeleteNoteCommand(Index.fromOneBased(1), Index.fromOneBased(persons.get(0).getNotes().size() + 1));

            assertThrows(CommandException.class, () -> command.execute(model));
        }

        @Test
        public void execute_success() throws CommandException {
            ObservableList<Person> persons = TypicalPersons.getTypicalAddressBook().getPersonList();

            Mockito.when(model.getFilteredPersonList()).thenReturn(new FilteredList<>(persons));
            DeleteNoteCommand command = new DeleteNoteCommand(Index.fromOneBased(1), Index.fromOneBased(1));

            CommandResult result = command.execute(model);

            Mockito.verify(model).setPerson(Mockito.any(Person.class), Mockito.any(Person.class));
            assertEquals(new CommandResult(
                    String.format(DeleteNoteCommand.MESSAGE_SUCCESS,
                        Messages.format(persons.get(0).getNotes().get(0)))),
                result);
        }
    }

    @Test
    public void equals_success() {
        DeleteNoteCommand deleteNoteCommand1 = new DeleteNoteCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        DeleteNoteCommand deleteNoteCommand2 = new DeleteNoteCommand(Index.fromOneBased(2), Index.fromOneBased(2));

        // Same object.
        assertEquals(deleteNoteCommand1, deleteNoteCommand1);

        // Same values.
        DeleteNoteCommand deleteNoteCommand1Copy = new DeleteNoteCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        assertEquals(deleteNoteCommand1, deleteNoteCommand1Copy);

        // Different types.
        assertNotEquals(1, deleteNoteCommand1);

        // Null.
        assertNotEquals(null, deleteNoteCommand1);

        // Different note.
        assertNotEquals(deleteNoteCommand1, deleteNoteCommand2);
    }

    @Test
    public void hashCode_success() {
        DeleteNoteCommand deleteNoteCommand1 = new DeleteNoteCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        DeleteNoteCommand deleteNoteCommand2 = new DeleteNoteCommand(Index.fromOneBased(1), Index.fromOneBased(1));

        // Same values.
        assertEquals(deleteNoteCommand1.hashCode(), deleteNoteCommand2.hashCode());

        // Different values.
        DeleteNoteCommand deleteNoteCommand3 = new DeleteNoteCommand(Index.fromOneBased(2), Index.fromOneBased(2));
        assertNotEquals(deleteNoteCommand1.hashCode(), deleteNoteCommand3.hashCode());
    }

    @Test
    public void getCommandWord_success() {
        AddNoteCommand addNoteCommand = new AddNoteCommand(Index.fromOneBased(1), VALID_NOTE_FLU);
        assertEquals(AddNoteCommand.COMMAND_WORD, addNoteCommand.getCommandWord());
    }

    @Test
    public void getMessageUsage_success() {
        AddNoteCommand addNoteCommand = new AddNoteCommand(Index.fromOneBased(1), VALID_NOTE_FLU);
        assertEquals(AddNoteCommand.MESSAGE_USAGE, addNoteCommand.getMessageUsage());
    }
}
