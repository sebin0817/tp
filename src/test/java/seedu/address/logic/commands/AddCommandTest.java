package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.note.Note;
import seedu.address.testutil.PersonBuilder;
public class AddCommandTest {
    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null, null));
    }
    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ReadOnlyAddressBook expectedPrevAddressBook = new AddressBook();
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        modelStub.setAddressBook(expectedPrevAddressBook);
        Person validPerson = new PersonBuilder().build();
        UndoableCommand addCommand = new AddCommand(validPerson, null);
        CommandResult commandResult = addCommand.execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(expectedPrevAddressBook, addCommand.getPrevAddressBookState());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson, null);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void undo_success() {
        AddressBook notExpectedAddressBook = new AddressBook();
        notExpectedAddressBook.addPerson(BOB);
        ModelStub modelStub = new ModelStub();
        modelStub.setAddressBook(notExpectedAddressBook);
        AddCommand addCommand = new AddCommand(ALICE, new AddressBook());
        CommandResult commandResult = addCommand.undo(modelStub);

        assertNotEquals(notExpectedAddressBook, modelStub.getAddressBook());
        assertEquals(AddCommand.MESSAGE_UNDO_ADD_SUCCESS, commandResult.getFeedbackToUser());
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice, null);
        AddCommand addBobCommand = new AddCommand(bob, null);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice, null);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));

        // different prevState -> returns false
        assertFalse(addAliceCommand.equals(new AddCommand(alice, new AddressBook())));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE, null);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    @Test
    public void getCommandWord() {
        AddCommand addCommand = new AddCommand(ALICE, null);
        assertEquals(AddCommand.COMMAND_WORD, addCommand.getCommandWord());
    }

    @Test
    public void getMessageUsage() {
        AddCommand addCommand = new AddCommand(ALICE, null);
        assertEquals(AddCommand.MESSAGE_USAGE, addCommand.getMessageUsage());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        private ReadOnlyAddressBook addressBook;
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addNote(Note note) {
            throw new AssertionError("This method should not be called.");
        }


        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
            this.addressBook = addressBook;
        }

        @Override
        public void setFilterPersonsPredicate(Predicate<Person> filterPersonsPredicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return this.addressBook;
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Note> getFilteredNoteList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonListWithCurrentPredicate() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredNoteList(Predicate<Note> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }
    }

}
