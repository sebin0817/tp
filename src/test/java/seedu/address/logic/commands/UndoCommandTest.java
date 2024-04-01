package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class UndoCommandTest {

    @Test
    public void execute_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS_UNDO, expectedModel);
    }
}
