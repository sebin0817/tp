package seedu.address.logic.commands;


import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_NOTES;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.note.Description;
import seedu.address.model.person.note.Note;

/**
 * Edits the details of an existing appointment note in the address book.
 * edit: edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]
 * edit-an: edit-an PATIENT_INDEX NOTE_INDEX [d/DD-MM-YYYY] [t/HHMM] [n/NOTE]
 */
public class EditNoteCommand extends Command {

    public static final String COMMAND_WORD = "edit-an";
    public static final Prefix PREFIX_DATE = new Prefix("d/");
    public static final Prefix PREFIX_TIME = new Prefix("t/");
    public static final Prefix PREFIX_NOTE = new Prefix("n/");

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the appointment note identified "
            + "by the patient index number used in the displayed patient list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: PATIENT_INDEX (must be a positive integer) "
            + "NOTE_INDEX (must be a positive integer) "
            + "[" + PREFIX_DATE + "DATE<DD-MM-YYYY>] "
            + "[" + PREFIX_TIME + "TIME<HHMM>] "
            + "[" + PREFIX_NOTE + "NOTE] "
            + "Example: " + COMMAND_WORD + " 1 1 "
            + PREFIX_DATE + "19-02-2024 "
            + PREFIX_TIME + "1430 "
            + PREFIX_NOTE + "General Flu ";

    public static final String MESSAGE_EDIT_NOTE_SUCCESS = "Edited Note: %1$s";
    public static final String MESSAGE_NOTE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_NOTE = "This note already exists in the address book.";


    private final Index patientIndex;
    private final Index noteIndex;
    private final EditNoteCommand.EditNoteDescriptor editNoteDescriptor;

    /**
     * @param patientIndex      of the patient with the appointment note to edit
     * @param noteIndex         of the note in the filtered note list to edit
     * @param editNoteDescriptor details to edit the note with
     */
    public EditNoteCommand(Index patientIndex, Index noteIndex, EditNoteCommand.EditNoteDescriptor editNoteDescriptor) {
        requireNonNull(patientIndex);
        requireNonNull(noteIndex);
        requireNonNull(editNoteDescriptor);

        this.patientIndex = patientIndex;
        this.noteIndex = noteIndex;
        this.editNoteDescriptor = new EditNoteCommand.EditNoteDescriptor(editNoteDescriptor);
    }

    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownPatientList = model.getFilteredPersonList();

        // Check if patientIndex exists & if noteIndex of the selected patient exists
        if (patientIndex.getZeroBased() >= lastShownPatientList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_INDEX);
        } else {
            ObservableList<Note> personNotes = lastShownPatientList.get(patientIndex.getZeroBased()).getNotes();
            if (noteIndex.getZeroBased() >= personNotes.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_INDEX);
            }
        }

        ObservableList<Note> personNotes = lastShownPatientList.get(patientIndex.getZeroBased()).getNotes();
        Note noteToEdit = personNotes.get(noteIndex.getZeroBased());
        Note editedNote = createEditedNote(noteToEdit, editNoteDescriptor);

        if (!noteToEdit.isSameNote(editedNote) && model.hasNote(editedNote)) {
            throw new CommandException(MESSAGE_DUPLICATE_NOTE);
        }

        model.setNote(noteToEdit, editedNote);
        model.updateFilteredNoteList(PREDICATE_SHOW_ALL_NOTES);
        return new CommandResult(String.format(MESSAGE_EDIT_NOTE_SUCCESS, Messages.format(editedNote)));
    }

    /**
     * Creates and returns a {@code Note} with the details of {@code noteToEdit}
     * edited with {@code editNoteDescriptor}.
     */
    private static Note createEditedNote(Note noteToEdit, EditNoteCommand.EditNoteDescriptor editNoteDescriptor) {
        assert noteToEdit != null;

        LocalDateTime updatedDateTime = editNoteDescriptor.getDateTime().orElse(noteToEdit.getDateTime());
        Description updatedDescription = editNoteDescriptor.getDescription().orElse(noteToEdit.getDescription());

        return new Note(updatedDateTime, updatedDescription);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditNoteCommand)) {
            return false;
        }

        EditNoteCommand otherEditNoteCommand = (EditNoteCommand) other;
        return patientIndex.equals(otherEditNoteCommand.patientIndex)
                && noteIndex.equals(otherEditNoteCommand.noteIndex)
                && editNoteDescriptor.equals(otherEditNoteCommand.editNoteDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("patientIndex", patientIndex)
                .add("noteIndex", noteIndex)
                .add("editNoteDescriptor", editNoteDescriptor)
                .toString();
    }

    public static class EditNoteDescriptor {
        private LocalDateTime dateTime;
        private Description description;

        public EditNoteDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditNoteDescriptor(EditNoteCommand.EditNoteDescriptor toCopy) {
            setDateTime(toCopy.dateTime);
            setDescription(toCopy.description);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(dateTime, description);
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public Optional<LocalDateTime> getDateTime() {
            return Optional.ofNullable(dateTime);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }


        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditNoteCommand.EditNoteDescriptor)) {
                return false;
            }

            EditNoteCommand.EditNoteDescriptor otherEditNoteDescriptor = (EditNoteCommand.EditNoteDescriptor) other;
            return Objects.equals(dateTime, otherEditNoteDescriptor.dateTime)
                    && Objects.equals(description, otherEditNoteDescriptor.description);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("dateTime", dateTime)
                    .add("description", description)
                    .toString();
        }
    }
}
