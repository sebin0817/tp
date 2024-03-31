package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertUndoableCommandExecuteSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertUndoableCommandUndoSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertUndoableCommandExecuteSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertUndoableCommandExecuteSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void getCommandWord() {
        Command command = new ClearCommand();
        assertEquals("clear", command.getCommandWord());
    }

    @Test
    public void getMessageUsage() {
        Command command = new ClearCommand();
        assertEquals("clear: Clears the patient book.\n"
                + "Example: clear", command.getMessageUsage());
    }

    @Test
    public void undo_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        AddressBook samplePrevState = new AddressBook();
        Model expectedModel = new ModelManager(samplePrevState, new UserPrefs());
        ClearCommand clearCommand = new ClearCommand(samplePrevState);

        assertUndoableCommandUndoSuccess(clearCommand, model,
                ClearCommand.MESSAGE_UNDO_CLEAR_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final ClearCommand clearCommand = new ClearCommand();

        // same object -> returns true
        assertTrue(clearCommand.equals(clearCommand));

        // null -> returns false
        assertFalse(clearCommand.equals(null));

        //different prevState -> returns false;
        assertFalse(clearCommand.equals(new ClearCommand(new AddressBook())));
    }
}
