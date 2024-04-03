package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_NOTES;

import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.note.Note;

/**
 * Deletes a note from a patient's list of notes.
 */
public class DeleteNoteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete-an";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the specified appointment note. "
        + "Parameters: PATIENT_INDEX (must be a positive integer) NOTE_INDEX (must be a positive integer) "
        + "Example: " + COMMAND_WORD + " 1 3 ";

    public static final String MESSAGE_SUCCESS = "Deleted Note: %1$s";

    public static final String MESSAGE_UNDO_DELETE_SUCCESS = "Deleted note undone.";

    private final Index patientIndex;
    private final Index noteIndex;

    /**
     * Creates a DeleteNoteCommand to delete the note at the specified {@code noteIndex} of the specified
     * {@code patientIndex}.
     */
    public DeleteNoteCommand(Index patientIndex, Index noteIndex) {
        super(null);
        requireAllNonNull(patientIndex, noteIndex);

        this.patientIndex = patientIndex;
        this.noteIndex = noteIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> patients = model.getFilteredPersonList();

        if (patientIndex.getZeroBased() >= patients.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_INDEX);
        }

        if (noteIndex.getZeroBased() >= patients.get(patientIndex.getZeroBased()).getNotes().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_INDEX);
        }

        Person person = patients.get(patientIndex.getZeroBased());
        Note note = person.getNotes().get(noteIndex.getZeroBased());

        ObservableList<Note> notes = FXCollections.observableArrayList(person.getNotes());
        notes.remove(noteIndex.getZeroBased());

        savePrevState(model);

        Person updatedPerson = new Person.Builder(person).setNotes(notes).build();
        model.setPerson(person, updatedPerson);

        model.updateFilteredNoteList(PREDICATE_SHOW_ALL_NOTES);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(note)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeleteNoteCommand that = (DeleteNoteCommand) o;
        return Objects.equals(patientIndex, that.patientIndex) && Objects.equals(noteIndex, that.noteIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientIndex, noteIndex);
    }

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public String getMessageUsage() {
        return MESSAGE_USAGE;
    }

    @Override
    public CommandResult undo(Model model) {
        model.setAddressBook(prevAddressBookState);
        return new CommandResult(MESSAGE_UNDO_DELETE_SUCCESS);
    }
}
